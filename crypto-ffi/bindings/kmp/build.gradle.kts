import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinMultiplatform
import gobley.gradle.GobleyHost
import gobley.gradle.cargo.dsl.*
import gobley.gradle.rust.targets.RustPosixTarget
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("multiplatform") version "2.0.21"
    id("com.android.library") version "8.2.2"
    id("dev.gobley.cargo") version "0.3.7"
    id("dev.gobley.uniffi") version "0.3.7"
    id("com.vanniktech.maven.publish.base") version "0.34.0"
    kotlin("plugin.atomicfu") version "2.0.21"
    id("org.jetbrains.dokka") version "2.0.0"
}

group = findProperty("GROUP") as String? ?: "io.github.mohamadjaara"
version = findProperty("VERSION_NAME") as String? ?: "9.1.1"

kotlin {
    // Explicit API mode for better library quality
    explicitApi()

    // Android target
    androidTarget {
        publishLibraryVariants("release")
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    // JVM target
    jvm {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    // iOS targets
    iosArm64()
    iosSimulatorArm64()

    // macOS ARM64 target
    macosArm64()

    // WASM JS target (stubs only for now)
    @OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }

    // JS target (stubs only for now)
    js {
        browser()
    }

    sourceSets {
        val commonMain by getting {
            kotlin.srcDir(projectDir.resolve("../shared/kotlin"))
            dependencies {
                implementation(libs.coroutines.core)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.coroutines.test)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(libs.jna)
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(libs.assertj.core)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation("${libs.jna.get()}@aar")
                implementation("androidx.annotation:annotation:1.9.1")
            }
        }

        val androidInstrumentedTest by getting {
            dependencies {
                implementation(libs.android.junit)
                implementation(libs.espresso)
                implementation(libs.assertj.core)
            }
        }

        // Native targets share sources
        val nativeMain by creating {
            dependsOn(commonMain)
        }

        val nativeTest by creating {
            dependsOn(commonTest)
        }

        val iosArm64Main by getting {
            dependsOn(nativeMain)
        }

        val iosArm64Test by getting {
            dependsOn(nativeTest)
        }

        val iosSimulatorArm64Main by getting {
            dependsOn(nativeMain)
        }

        val iosSimulatorArm64Test by getting {
            dependsOn(nativeTest)
        }

        val macosArm64Main by getting {
            dependsOn(nativeMain)
        }

        val macosArm64Test by getting {
            dependsOn(nativeTest)
        }
    }
}

android {
    namespace = "io.github.mohamadjaara.crypto"
    compileSdk = libs.versions.sdk.compile.get().toInt()

    defaultConfig {
        minSdk = libs.versions.sdk.min.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        ndk {
            abiFilters += setOf("arm64-v8a", "armeabi-v7a", "x86_64")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

cargo {
    // Point to the crypto-ffi crate directory
    packageDirectory = layout.projectDirectory.dir("../..")

    // Configure features
    features.addAll("proteus")

    // Only build JVM native libraries for the current host platform
    // This disables cross-compilation for other JVM targets (e.g., Linux ARM64 on macOS)
    builds.jvm {
        embedRustLibrary = (rustTarget == GobleyHost.current.rustTarget)
    }
}

// JVM Native Library Bundling
//
// The Gobley plugin's embedRustLibrary doesn't properly bundle native libraries
// into the JVM JAR. We need to manually copy and include them as resources.

val buildType = if (System.getenv("RELEASE") == "1") "release" else "debug"

// Directory where we'll collect native libraries for JVM resources
val jvmNativeLibsDir = layout.buildDirectory.dir("jvmNativeLibs/$buildType")

// Task to copy native libraries for JVM target
val copyJvmNativeLibraries by tasks.registering {
    description = "Copies native libraries into JVM resources directory"
    group = "build"

    // Declare outputs for up-to-date checking
    outputs.dir(jvmNativeLibsDir)

    doLast {
        val libs = listOf(
            Triple("x86_64-unknown-linux-gnu", "linux-x86-64", "so"),
            Triple("aarch64-apple-darwin", "darwin-aarch64", "dylib")
        )
        libs.forEach { (rustTarget, jvmTarget, ext) ->
            val libName = "libcore_crypto_ffi.$ext"
            val src = projectDir.resolve("../../../target/$rustTarget/$buildType/$libName")
            val destDir = jvmNativeLibsDir.get().asFile.resolve(jvmTarget)
            val dest = destDir.resolve(libName)
            // Copy if exists locally, or fail on CI if missing
            if (src.exists()) {
                destDir.mkdirs()
                src.copyTo(dest, overwrite = true)
                logger.lifecycle("Copied native library: $src -> $dest")
            } else if (System.getenv("CI") != null) {
                throw GradleException("Native library not found on CI: $src")
            } else {
                logger.warn("Native library not found (skipping): $src")
            }
        }
    }
}

// Wire up the copy task to run before JVM resource processing
tasks.matching { it.name == "jvmProcessResources" }.configureEach {
    dependsOn(copyJvmNativeLibraries)
}

// Add native libraries directory to JVM resources
kotlin {
    sourceSets {
        val jvmMain by getting {
            resources.srcDir(jvmNativeLibsDir)
        }
    }
}

// Configure iOS build tasks with the required environment
afterEvaluate {
    tasks.matching { it.name.contains("cargoBuildIos") }.configureEach {
        // Set environment via the task's additionalEnvironment if available
        if (this is gobley.gradle.cargo.tasks.CargoBuildTask) {
            val target = if (name.contains("Simulator")) {
                "IPHONESIMULATOR_DEPLOYMENT_TARGET"
            } else {
                "IPHONEOS_DEPLOYMENT_TARGET"
            }
            additionalEnvironment.put(target, "16.0")
        }
    }
}

uniffi {
    generateFromLibrary {
        namespace = "core_crypto_ffi"
        packageName = "com.wire.crypto"
    }
}

mavenPublishing {
    publishToMavenCentral(automaticRelease = true)
    pomFromGradleProperties()
    signAllPublications()
    // Configure KMP publishing with Dokka-generated javadocs
    configure(KotlinMultiplatform(javadocJar = JavadocJar.Dokka("dokkaHtml")))
}

// Allows skipping signing jars published to 'MavenLocal' repository
tasks.withType<Sign>().configureEach {
    if (System.getenv("CI") == null) {
        enabled = false
    }
}

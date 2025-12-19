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
}

// Allows skipping signing jars published to 'MavenLocal' repository
tasks.withType<Sign>().configureEach {
    if (System.getenv("CI") == null) {
        enabled = false
    }
}

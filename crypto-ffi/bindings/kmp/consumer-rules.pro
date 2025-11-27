# ProGuard rules for core-crypto-kmp

# Keep UniFFI generated classes
-keep class uniffi.** { *; }
-keep class com.wire.crypto.** { *; }

# Keep JNA classes used by UniFFI
-keep class com.sun.jna.** { *; }
-keepclassmembers class * extends com.sun.jna.Structure {
    public *;
}

# Keep native method names
-keepclasseswithmembernames,includedescriptorclasses class * {
    native <methods>;
}

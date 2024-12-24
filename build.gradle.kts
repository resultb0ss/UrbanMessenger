buildscript {
    repositories {
        google()
        maven { url = uri("https://jitpack.io") }
    }
    dependencies {
        classpath(libs.gradle)
        classpath(libs.androidx.navigation.safe.args.gradle.plugin)
    }
}

plugins {
    id("com.android.application") version "8.4.2" apply false
    id("org.jetbrains.kotlin.android") version "2.0.20" apply false
    id("androidx.navigation.safeargs.kotlin") version "2.8.5" apply false
    id("org.jetbrains.kotlin.plugin.parcelize") version "1.8.20" apply false
    id("com.google.devtools.ksp") version "2.0.20-1.0.24" apply false
    id("com.google.dagger.hilt.android") version "2.52" apply false
    alias(libs.plugins.google.gms.google.services) apply false
}

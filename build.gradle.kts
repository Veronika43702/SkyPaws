// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.kotlinKsp) apply false
    alias(libs.plugins.hilt.android.gradle.plugin) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.dokka) apply true
}

buildscript {
    extra["kotlin_version"] = libs.versions.kotlin
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.dokka.gradle.plugin)
    }
}

apply(plugin = "org.jetbrains.dokka")

subprojects {
    apply(plugin = "org.jetbrains.dokka")
}
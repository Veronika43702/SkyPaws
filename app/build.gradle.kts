import java.io.File


plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlinKsp)
    alias(libs.plugins.hilt.android.gradle.plugin)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "ru.skypaws.mobileapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "ru.skypaws.mobileapp"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "v0.1.6"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField("int", "DB_VERSION", "1")
    }

    signingConfigs {
        getByName("debug") {
            storeFile = file("debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
    }


    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            manifestPlaceholders["usesCleartextTraffic"] = "false"
        }

        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs["debug"]
            manifestPlaceholders["usesCleartextTraffic"] = "true"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_22
        targetCompatibility = JavaVersion.VERSION_22
    }
    kotlinOptions {
        jvmTarget = "22"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}


// Write DB_VERSION and versionName to a file
tasks.register("writeDbVersionAndVersionName") {
    doLast {
        val buildConfigFile = File(project.layout.buildDirectory.get().asFile, "generated/source/buildConfig/debug/ru/skypaws/mobileapp/BuildConfig.java")
        if (buildConfigFile.exists()) {
            val buildConfigContent = buildConfigFile.readText()
            val dbVersion = Regex("public static final int DB_VERSION = (\\d+);").find(buildConfigContent)?.groupValues?.get(1)?.toIntOrNull() ?: 1
            val versionName = Regex("public static final String VERSION_NAME = \"v([0-9]+\\.[0-9]+\\.[0-9]+)\";").find(buildConfigContent)?.groupValues?.get(1) ?: "unknown"


            val dbVersionFile = File("${project.projectDir}/src/main/assets/", "Versions.txt")
            dbVersionFile.parentFile.mkdirs()
            dbVersionFile.writeText("$dbVersion\nv$versionName")
        } else {
            println("BuildConfig file not found. Skipping DB_VERSION and versionName file creation.")
        }
    }
}

tasks.named("preBuild").configure { dependsOn("writeDbVersionAndVersionName") }


dependencies {
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":presentation"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.material)
    implementation(libs.androidx.material3)

    testImplementation(libs.jetbrains.kotlinx.coroutines.test)
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.bytebuddy.byte.buddy)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.play.services)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
}
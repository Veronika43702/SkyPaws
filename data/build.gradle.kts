plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlinKsp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.protobuf)
}

apply {
    from("$rootDir/build-setups/base.gradle")
}


android {
    namespace = "ru.skypaws.data"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        buildConfigField("String", "BASE_URL", "\"https://www.skypaws.ru/\"")
        buildConfigField("String", "BASE_URL_API", "\"https://www.skypaws.ru/api/v1/\"")
        buildConfigField("String", "AVIABIT_URL", "\"https://aviabit.pobeda.aero/\"")
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:4.28.3"
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                create("kotlin")
                create("java")
            }
        }
    }
}

dependencies {
    implementation(libs.bundles.base)
    implementation(libs.androidx.documentfile)
    implementation(libs.coroutines.android)
    implementation(libs.bundles.datastore)
    implementation(libs.bundles.network)
    implementation(libs.bundles.database)
    ksp(libs.androidx.room.compiler)
    implementation(libs.bundles.hilt)
    ksp(libs.hilt.compiler)
    testImplementation(libs.junit)

    implementation(project(":domain"))
}
import java.io.File

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlinKsp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.serialization)
}

apply {
    from("$rootDir/build-setups/base.gradle")
    from("$rootDir/build-setups/app.gradle")
    from("$rootDir/build-setups/compose.gradle")
    from("$rootDir/build-setups/ext.gradle")
}

android {
    namespace = "ru.skypaws.mobileapp"
}

dependencies {
    implementation(libs.bundles.base)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.bundles.compose)
    implementation(libs.bundles.coroutines)
    implementation(libs.bundles.hilt)
    implementation(platform(libs.androidx.compose.bom))
    ksp(libs.hilt.compiler)
    androidTestImplementation(libs.bundles.android.tests)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    testImplementation(libs.bundles.tests)
    debugImplementation(libs.bundles.debug)
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":presentation"))
}

// Write DB_VERSION and versionName to a file
tasks.register("writeDbVersionAndVersionName") {
    doLast {
        val dbVersion = project.ext.properties.getValue("version_db")
        val versionName = project.ext.properties.getValue("version_name")

        val dbVersionFile = File("${project.projectDir}/src/main/assets/", "Versions.txt")

        dbVersionFile.parentFile.mkdirs()
        dbVersionFile.writeText("$dbVersion\nv$versionName")
    }
}

tasks.named("preBuild").configure { dependsOn("writeDbVersionAndVersionName") }
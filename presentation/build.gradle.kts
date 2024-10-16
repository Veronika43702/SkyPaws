plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlinKsp)
    alias(libs.plugins.hilt)
}

apply {
    from("$rootDir/build-setups/base.gradle")
    from("$rootDir/build-setups/compose.gradle")
}

android {
    namespace = "ru.skypaws.modileapp"
}

dependencies {
    implementation(libs.bundles.base)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.bundles.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.coroutines)
    implementation(libs.bundles.hilt)
    ksp(libs.hilt.compiler)

    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":feature-enter"))
    implementation(project(":feature-main"))
    implementation(project(":core-presentation"))
}
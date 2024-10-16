plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.kotlinKsp)
    alias(libs.plugins.kotlin.serialization)
}

java {
    sourceCompatibility = JavaVersion.VERSION_22
    targetCompatibility = JavaVersion.VERSION_22
}

dependencies {
    implementation(libs.org.jetbrains.kotlin.kotlin.stdlib9)
    implementation(libs.coroutines)
    implementation(libs.dagger)
    ksp(libs.dagger.compiler)
}

tasks.withType<Jar> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    exclude("META-INF/gradle/incremental.annotation.processors")
}
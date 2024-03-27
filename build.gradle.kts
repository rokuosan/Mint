plugins {
    alias(libs.plugins.jvm)
    alias(libs.plugins.kotlinx.serialization)
}

group = "me.konso"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.bundles.kotlinx)
    implementation(libs.clikt)
    implementation(libs.napier)
    implementation(libs.ktor.core)
    implementation(libs.ktor.client.java)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.slf4j.nop)

    testImplementation(libs.bundles.test)
}

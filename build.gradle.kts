import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.7.10"
    kotlin("plugin.serialization") version "1.7.10"
    id("org.jetbrains.dokka") version "1.7.10"
    id ("com.github.johnrengelman.shadow") version "7.1.2"
    application
    java
}

group = "com.deviseworks"
java.sourceCompatibility = JavaVersion.VERSION_16
java.targetCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    // Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.0")

    // Dokka
    dokkaHtmlPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:1.7.10")

    // Test
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

application {
    // Define the main class for the application.
    mainClass.set("com.deviseworks.instantInstance.AppKt")
}

tasks.withType<KotlinCompile>{
    kotlinOptions{
        jvmTarget = "11"
    }
}

tasks.dokkaHtml.configure{
    outputDirectory.set(buildDir.resolve("docs"))
    moduleName.set("Instant Instance Tools")
}

tasks.getByName<JavaExec>("run"){
    standardInput = System.`in`
}
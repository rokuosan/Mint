import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.10"
    kotlin("plugin.serialization") version "1.6.21"
    id("org.jetbrains.dokka") version "1.9.10"
    id ("com.github.johnrengelman.shadow") version "8.1.1"
    application
    java
}

group = "me.konso"
java.sourceCompatibility = JavaVersion.VERSION_16
java.targetCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0-RC")
    implementation("com.github.ajalt.clikt:clikt:4.2.1")

    dokkaHtmlPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:1.9.10")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

application {
    mainClass.set("io.github.rokuosan.mint.MainKt")
}

tasks.withType<KotlinCompile>{
    kotlinOptions{
        jvmTarget = "11"
    }
}

tasks.dokkaHtml.configure{
    outputDirectory.set(layout.buildDirectory.dir("docs"))
    moduleName.set("Mint")
}

tasks.getByName<JavaExec>("run"){
    standardInput = System.`in`
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "11"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "11"
}
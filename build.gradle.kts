import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.9.10"
    kotlin("plugin.serialization") version "1.9.10"
    id("org.jetbrains.dokka") version "1.9.10"
    id ("com.github.johnrengelman.shadow") version "8.1.1"
    application
    java
}

//group = "com.deviseworks"
group = "me.konso"
java.sourceCompatibility = JavaVersion.VERSION_16
java.targetCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.10")
    implementation("com.github.ajalt.clikt:clikt:4.2.0")

    dokkaHtmlPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:1.9.10")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

application {
    mainClass.set("io.github.rokuosan.mint")
}

tasks.withType<KotlinCompile>{
    kotlinOptions{
        jvmTarget = "11"
    }
}

tasks.dokkaHtml.configure{
    outputDirectory.set(buildDir.resolve("docs"))
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
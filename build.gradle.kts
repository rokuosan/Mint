plugins {
    id("org.jetbrains.kotlin.jvm") version "1.7.10"
    application
}

repositories {
    mavenCentral()
}

dependencies {
    // Test
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

application {
    // Define the main class for the application.
    mainClass.set("com.deviseworks.instantInstance.AppKt")
}

plugins {
    kotlin("jvm") version "1.6.10"
    id("org.openjfx.javafxplugin") version "0.0.8"
}

group = "vitorscoelho.impl.javafx"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":core"))
    implementation("no.tornado:tornadofx:1.7.20")
    runtimeOnly ("org.openjfx:javafx-graphics:17.0.2:win")
    runtimeOnly ("org.openjfx:javafx-graphics:17.0.2:linux")
    runtimeOnly ("org.openjfx:javafx-graphics:17.0.2:mac")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions { jvmTarget = "1.8" }
}

javafx {
    version = "11"
    modules = listOf("javafx.controls", "javafx.fxml", "javafx.graphics")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
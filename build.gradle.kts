import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val kotlinVersion = "2.0.20"
val javaVersion = 21
val javafxVersion = "22.0.2"

plugins {
    kotlin("jvm") version "2.0.20"
    id("org.openjfx.javafxplugin") version "0.0.14"
    id("org.jetbrains.kotlin.plugin.spring") version "2.0.20"
    id("org.springframework.boot") version "3.3.3"
    application
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
    maven { url = uri("https://repo.spring.io/snapshot") }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.springframework.boot:spring-boot-starter") {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-web")
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-webflux")
    }
    implementation(platform("org.springframework.ai:spring-ai-bom:1.0.0-SNAPSHOT"))
    implementation("org.openjfx:javafx-controls:17.0.2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.0")
    implementation("org.springframework.ai:spring-ai-openai")
    implementation("org.springframework.ai:spring-ai-openai-spring-boot-starter")
}

javafx {
    version = javafxVersion
    modules = listOf("javafx.controls")
}

application {
    mainClass.set("com.appga.contentcraft.ApplicationKt")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions {
        jvmTarget.set(JvmTarget.fromTarget(javaVersion.toString()))
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(javaVersion))
    }
}
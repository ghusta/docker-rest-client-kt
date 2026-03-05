import org.gradle.kotlin.dsl.implementation
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.3.0"
    id("com.google.devtools.ksp") version "2.3.5"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.moshi)
    implementation(libs.moshi.kotlin)
    implementation(libs.moshi.adapters)
    implementation(libs.okhttp.core)
    implementation(libs.okhttp.logging)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")

    // see https://kotlinlang.org/docs/ksp-quickstart.html
    ksp(libs.moshi.kotlin.codegen)

    testImplementation(kotlin("test"))

    testImplementation(platform(libs.assertj.bom))
    testImplementation(libs.assertj.core)
}

kotlin {
    jvmToolchain(17)
}

tasks.test {
    useJUnitPlatform()
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.compilerOptions {
    freeCompilerArgs.set(listOf("-Xannotation-default-target=param-property"))
}
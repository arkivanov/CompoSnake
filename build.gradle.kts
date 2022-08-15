import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    id("org.jetbrains.compose") version "1.1.0"
}

group = "me.aivanov"
version = "1.0"

repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
}

dependencies {
    implementation(compose.desktop.currentOs)
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

subprojects {
    val compilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
    tasks.withType<KotlinCompile> {
        kotlinOptions.freeCompilerArgs += compilerArgs
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompileCommon> {
        kotlinOptions.freeCompilerArgs = compilerArgs
    }
}

compose.desktop {
    application {
        mainClass = "com.arkivanov.composnake.MainKt"
        jvmArgs.add("-Dsun.java2d.uiScale=2.0")
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "composnake"
            windows.msiPackageVersion = "1.0.0"
        }
    }
}

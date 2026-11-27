import godot.gradle.GodotLanguage
import org.jetbrains.gradle.ext.packagePrefix
import org.jetbrains.gradle.ext.settings

plugins {
    kotlin("jvm") version libs.versions.kotlin
    id("org.jetbrains.gradle.plugin.idea-ext") version "1.4.1"
    id("com.utopia-rise.godot-jvm") version "1.0.1"
    kotlin("plugin.serialization") version "2.4.21"
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.12.0")
    implementation("org.jetbrains.kotlinx:kotlinx-io-core:0.9.3")
}

repositories {
    mavenCentral()
}

godot {
    languages.set(setOf(GodotLanguage.KOTLIN, GodotLanguage.JAVA, GodotLanguage.SCALA))
    toolchain.kotlinVersion.set(libs.versions.kotlin)
    isGodotCoroutinesEnabled.set(true)
}

sourceSets.main {
    kotlin.srcDir("game")
}

idea.module.settings {
    packagePrefix["game"] = "game"
}

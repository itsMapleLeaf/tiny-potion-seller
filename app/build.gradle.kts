import godot.gradle.GodotLanguage
import org.jetbrains.gradle.ext.packagePrefix
import org.jetbrains.gradle.ext.settings

plugins {
    kotlin("jvm") version libs.versions.kotlin
    id("org.jetbrains.gradle.plugin.idea-ext") version "1.4.1"
    id("com.utopia-rise.godot-jvm") version "1.0.1"
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

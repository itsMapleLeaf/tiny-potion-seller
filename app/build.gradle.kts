import godot.gradle.GodotLanguage

plugins {
    kotlin("jvm") version libs.versions.kotlin
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

sourceSets {
    main {
        kotlin {
            setSrcDirs(listOf("game"))
        }
    }
}

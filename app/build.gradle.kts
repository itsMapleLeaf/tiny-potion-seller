import godot.gradle.GodotLanguage

plugins {
    id("com.utopia-rise.godot-jvm") version "1.0.1"
}

repositories {
    mavenCentral()
}

godot {
    languages.set(setOf(GodotLanguage.KOTLIN, GodotLanguage.JAVA, GodotLanguage.SCALA))

    isGodotCoroutinesEnabled.set(true)
}

sourceSets {
    main {
        kotlin {
            setSrcDirs(listOf("game"))
        }
    }
}

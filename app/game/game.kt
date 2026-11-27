package game

import game.data.GameState
import game.data.Ingredient
import game.shared.*
import godot.api.FileAccess
import godot.api.Time
import godot.core.Error
import godot.global.GD
import kotlinx.serialization.json.Json

private const val SAVE_FILE_PATH = "user://game.json"

private val json = Json {
    allowStructuredMapKeys = true
    prettyPrint = true
    prettyPrintIndent = "\t"
}

object Game {
    var state = GameState()

    fun loadGameState() {
        var content: String? = null
        state = try {
            content = getFileAsStringOrThrow(SAVE_FILE_PATH)
            val decoded = json.decodeFromString<GameState>(content)
            decoded
        } catch (ex: Exception) {
            when (ex) {
                is FileAccessException if ex.error == Error.FILE_NOT_FOUND -> {
                    GD.print("Creating new save file")
                    GameState.init().also { save() }
                }

                else -> {
                    GD.print("Existing safe file corrupt; backing up the save and starting a new game")
                    GD.print("Error: $ex")
                    GD.print("File Content: $content")
                    createNewGameWithBackup()
                }
            }
        }
    }

    fun createNewGameWithBackup(): GameState {
        try {
            copyFile(SAVE_FILE_PATH, getBackupPath())
        } catch (ex: Exception) {
            GD.pushError("Failed to create backup: $ex")
        }
        return GameState.init()
    }

    fun save() {
        FileAccess.open(SAVE_FILE_PATH, FileAccess.ModeFlags.WRITE).use {
            storeString(json.encodeToString(state)).checkFileAccessError()
        }
    }

    fun addIngredientToCauldron(ingredient: Ingredient) {
        state.run {
            ingredients.transfer(ingredient, cauldron.ingredients)
        }
        save()
    }

    fun removeIngredientFromCauldron(ingredient: Ingredient) {
        state.run {
            cauldron.ingredients.transfer(ingredient, ingredients)
        }
        save()
    }

    fun brew() {
        state.run {
            val result = cauldron.getBrewResult() ?: return
            potions.add(result.potion, result.count)
            cauldron.ingredients.clear()
        }
        save()
    }

    private fun getBackupPath(): String {
        val timestamp = Time.getUnixTimeFromSystem().times(1000).toUInt()
        return "user://game.$timestamp.json"
    }
}

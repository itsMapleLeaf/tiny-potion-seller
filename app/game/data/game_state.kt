package game.data

import game.shared.*
import godot.api.FileAccess
import godot.api.Time
import godot.core.Error
import godot.global.GD
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json


private const val SAVE_FILE_PATH = "user://game.json"

private val json = Json {
    allowStructuredMapKeys = true
    prettyPrint = true
    prettyPrintIndent = "\t"
}

@Serializable
class GameState {
    var dayNumber = 1
    var timeRemaining = 6
    var gold = 300

    val ingredients = Inventory<Ingredient>()
    val potions = Inventory<Potion>()
    val cauldrons = Inventory<CauldronSpec>()

    var cauldron = CauldronState(CauldronSpec.Wooden)

    var haggle: HaggleState? = null

    fun save(): GameState {
        FileAccess.open(SAVE_FILE_PATH, FileAccess.ModeFlags.WRITE).use {
            it.storeString(json.encodeToString(this)).checkFileAccessError()
        }
        return this
    }

    fun startHaggle(potion: Potion) {
        haggle = HaggleState(potion)
    }

    fun HaggleState.closeDeal() {
        gold += price
        haggle = null
    }

    fun HaggleState.retry() {
        haggle = HaggleState(this.potion)
    }

    companion object {
        fun newGame() = GameState().apply {
            ingredients.add(Ingredient.Feyberry, 10)
            ingredients.add(Ingredient.MandrakeRoot, 10)
            ingredients.add(Ingredient.SackOfSlime, 10)
        }

        fun loadOrCreate(): GameState {
            return try {
                val content = getFileAsStringOrThrow(SAVE_FILE_PATH)
                val decoded = json.decodeFromString<GameState>(content)
                decoded
            } catch (ex: Exception) {
                when (ex) {
                    is FileAccessException if ex.error == Error.FILE_NOT_FOUND -> {
                        GD.print("Creating new save file")
                        newGame().save()
                    }

                    else -> {
                        GD.print("Existing safe file corrupt; backing up the save and starting a new game")
                        GD.print("Error: $ex")
                        backupWithEmptyGame()
                    }
                }
            }
        }


        private fun backupWithEmptyGame(): GameState {
            try {
                copyFile(SAVE_FILE_PATH, getBackupPath())
            } catch (ex: Exception) {
                GD.pushError("Failed to create backup: $ex")
            }
            return newGame()
        }

        private fun getBackupPath(): String {
            val timestamp = Time.getUnixTimeFromSystem().times(1000).toUInt()
            return "user://game.$timestamp.json"
        }

    }


}

package game

import game.data.GameData
import game.data.IngredientSpec
import game.data.PotionData
import game.data.PotionSpec
import game.data.PotionTier
import game.extensions.addCount
import game.extensions.ensureNodeAs
import game.extensions.lazyNode
import game.screens.BrewingScreen
import godot.annotation.Script
import godot.api.Button
import godot.api.Control
import godot.api.InputEvent
import godot.api.InputEventKey
import godot.api.Node
import godot.extension.getNodeAs

@Script
class Main : Node() {
    val gameData = GameData().apply {
        ingredients.addCount(IngredientSpec.Feyberry, 10)
        ingredients.addCount(IngredientSpec.MandrakeRoot, 10)
        ingredients.addCount(IngredientSpec.SackOfSlime, 10)
    }

    private val brewingScreen by lazyNode<BrewingScreen>("%BrewingScreen")

    override fun _ready() {
        brewingScreen.gameData = gameData
        brewingScreen.refresh()
    }

    override fun _unhandledKeyInput(event: InputEvent) {
        if (event.isActionPressed("debug_quit")) {
            getTree()!!.quit()
        }
    }
}

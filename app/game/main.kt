package game

import game.data.GameData
import game.data.IngredientSpec
import game.extensions.addCount
import game.extensions.lazyNode
import game.components.BrewingPanel
import godot.annotation.Script
import godot.api.InputEvent
import godot.api.Node

@Script
class Main : Node() {
    val gameData = GameData().apply {
        ingredients.addCount(IngredientSpec.Feyberry, 10)
        ingredients.addCount(IngredientSpec.MandrakeRoot, 10)
        ingredients.addCount(IngredientSpec.SackOfSlime, 10)
    }

    private val brewingScreen by lazyNode<BrewingPanel>("%BrewingScreen")

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

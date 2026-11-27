package game

import game.components.BrewingPanel
import game.data.GameState
import game.data.Ingredient
import game.shared.lazyNode
import godot.annotation.Script
import godot.api.InputEvent
import godot.api.Label
import godot.api.Node
import godot.extension.connectLambda


@Script
class Main : Node() {
    private var gameState = GameState.loadOrCreate()

    private val goldCountLabel by lazyNode<Label>("%GoldCountLabel")
    private val brewingPanel by lazyNode<BrewingPanel>("%BrewingPanel")

    override fun _ready() {
        brewingPanel.gameState = gameState

        brewingPanel.brew.connectLambda { brew() }
        brewingPanel.addIngredientToCauldron.connectLambda { addIngredientToCauldron(it) }
        brewingPanel.removeIngredientFromCauldron.connectLambda { removeIngredientFromCauldron(it) }

        refresh()
    }

    override fun _unhandledKeyInput(event: InputEvent) {
        if (event.isActionPressed("debug_quit")) {
            getTree()!!.quit()
        }
    }

    private fun refresh() {
        goldCountLabel.text = gameState.gold.toString()
        brewingPanel.refresh()
    }


    private fun addIngredientToCauldron(ingredient: Ingredient) {
        gameState.update {
            ingredients.transfer(ingredient, cauldron.ingredients)
        }
    }

    private fun removeIngredientFromCauldron(ingredient: Ingredient) {
        gameState.update {
            cauldron.ingredients.transfer(ingredient, ingredients)
        }
    }

    private fun brew() {
        gameState.update {
            val result = cauldron.getBrewResult() ?: return@update
            potions.add(result.potion, result.count)
            cauldron.ingredients.clear()
        }
    }

    private fun GameState.update(block: GameState.() -> Unit) {
        block()
        save()
        refresh()
    }
}

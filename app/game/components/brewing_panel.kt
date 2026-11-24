package game.components

import game.data.GameState
import game.shared.lazyNode
import godot.annotation.Script
import godot.api.Button
import godot.api.Control
import godot.extension.connectLambda

@Script
class BrewingPanel : Control() {
    private val ingredientList by lazyNode<Control>("%IngredientList")
    private val cauldronIngredientList by lazyNode<Control>("%CauldronIngredientList")
    private val potionList by lazyNode<Control>("%PotionList")
    private val brewButton by lazyNode<Button>("%BrewButton")
    private val brewingResultPanel by lazyNode<BrewingResultPanel>("%BrewingResultPanel")


    override fun _ready() {
        brewButton.pressed.connectLambda {
            GameState.brew()
            refresh()
        }

        refresh()
    }

    fun refresh() {
        GameState.run {
            ingredientStock.recreateItemTiles(ingredientList) {
                addIngredientToCauldron(it)
                refresh()
            }

            cauldron.ingredients.recreateItemTiles(cauldronIngredientList) {
                removeIngredientFromCauldron(it)
                refresh()
            }

            potionStock.recreateItemTiles(potionList) {}

            brewingResultPanel.result = cauldron.getBrewResult()
        }
    }
}

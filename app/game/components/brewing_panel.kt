package game.components

import game.Game
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
            Game.brew()
            refresh()
        }
    }

    fun refresh() {
        Game.state.run {
            ingredients.recreateItemTiles(ingredientList) {
                Game.addIngredientToCauldron(it)
                refresh()
            }

            cauldron.ingredients.recreateItemTiles(cauldronIngredientList) {
                Game.removeIngredientFromCauldron(it)
                refresh()
            }

            potions.recreateItemTiles(potionList) {}

            brewingResultPanel.result = cauldron.getBrewResult()
        }
    }
}

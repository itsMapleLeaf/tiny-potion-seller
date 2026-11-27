package game.components

import game.data.GameState
import game.data.Ingredient
import game.shared.lazyNode
import godot.annotation.Script
import godot.api.Button
import godot.api.Control
import godot.core.signal0
import godot.core.signal1
import godot.extension.connectLambda

@Script
class BrewingPanel : Control() {
    val brew by signal0()
    val addIngredientToCauldron by signal1<Ingredient>()
    val removeIngredientFromCauldron by signal1<Ingredient>()

    private val ingredientList by lazyNode<Control>("%IngredientList")
    private val cauldronIngredientList by lazyNode<Control>("%CauldronIngredientList")
    private val potionList by lazyNode<Control>("%PotionList")
    private val brewButton by lazyNode<Button>("%BrewButton")
    private val brewingResultPanel by lazyNode<BrewingResultPanel>("%BrewingResultPanel")


    override fun _ready() {
        brewButton.pressed.connectLambda { brew.emit() }
    }

    fun render(gameState: GameState) {
        gameState.run {
            ingredients.renderItemTiles(ingredientList) {
                addIngredientToCauldron.emit(it)
            }

            cauldron.ingredients.renderItemTiles(cauldronIngredientList) {
                removeIngredientFromCauldron.emit(it)
            }

            potions.renderItemTiles(potionList) {}

            brewingResultPanel.result = cauldron.getBrewResult()
        }
    }
}

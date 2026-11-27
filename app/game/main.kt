package game

import game.components.BrewingPanel
import game.components.StockPanel
import game.data.GameState
import game.data.Ingredient
import game.shared.ensureNodeAs
import game.shared.lazyNode
import godot.annotation.Script
import godot.api.*
import godot.extension.connectLambda


@Script
class Main : Node() {
    private val gameState = GameState.loadOrCreate()

    private val goldCountLabel by lazyNode<Label>("%GoldCountLabel")
    private val stockPanel by lazyNode<StockPanel>("%StockPanel")
    private val brewingPanel by lazyNode<BrewingPanel>("%BrewingPanel")
    private val shopPanel by lazyNode<Control>("%ShopPanel")
    private val ingredientStorePanel by lazyNode<Control>("%IngredientStorePanel")
    private val adventureGuildPanel by lazyNode<Control>("%AdventureGuildPanel")
    private val placeButtonsContainer by lazyNode<Control>("%Places")

    private val places by lazy {
        sortedMapOf(
            "Brewing" to brewingPanel,
            "OpenShop" to shopPanel,
            "Ingredients" to ingredientStorePanel,
            "Adventure" to adventureGuildPanel,
        ).map { (buttonName, panel) ->
            val button = placeButtonsContainer.ensureNodeAs<Button>(buttonName)
            Place(button, panel)
        }
    }

    override fun _ready() {
        brewingPanel.brew.connectLambda { brew() }
        brewingPanel.removeIngredientFromCauldron.connectLambda { removeIngredientFromCauldron(it) }

        places[0].show()

        update()
    }

    override fun _unhandledKeyInput(event: InputEvent) {
        if (event.isActionPressed("debug_quit")) {
            getTree()!!.quit()
        }
    }

    private fun update() {
        goldCountLabel.text = gameState.gold.toString()
        brewingPanel.update(gameState)

        stockPanel.renderSections {
            renderSection("Ingredients", gameState.ingredients) {
                addIngredientToCauldron(it)
            }
            renderSection("Potions", gameState.potions) {
                // nothing yet!
            }
        }
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
        update()
    }

    private class Place(val button: Button, val panel: Control) {
        init {
            instances.add(this)
            button.toggled.connectLambda { on -> if (on) show() }
        }

        companion object {
            private val instances = mutableSetOf<Place>()
        }

        fun show() {
            for (place in instances) {
                if (place == this) {
                    place.panel.show()
                    place.button.setPressedNoSignal(true)
                } else {
                    place.panel.hide()
                    place.button.setPressedNoSignal(false)
                }
            }
        }
    }
}

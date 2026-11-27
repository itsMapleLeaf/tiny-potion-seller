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

    private val placeList = mutableListOf<Place>()
    private var currentPlace: Place? = null

    private val places by lazy {
        object {
            val brewingPlace = makePlace("Brewing", brewingPanel)
            val openShopPlace = makePlace("OpenShop", shopPanel)
            val ingredientsPlace = makePlace("Ingredients", ingredientStorePanel)
            val adventurePlace = makePlace("Adventure", adventureGuildPanel)
        }
    }

    private class Place(val button: Button, val panel: Control)

    override fun _ready() {
        brewingPanel.brew.connectLambda { brew() }
        brewingPanel.removeIngredientFromCauldron.connectLambda { removeIngredientFromCauldron(it) }

        showPlace(places.brewingPlace)

        update()
    }

    override fun _unhandledKeyInput(event: InputEvent) {
        if (event.isActionPressed("debug_quit")) {
            getTree()!!.quit()
        }
    }

    private fun makePlace(name: String, panel: Control): Place {
        val button = placeButtonsContainer.ensureNodeAs<Button>(name)
        val place = Place(button, panel)
        placeList.add(place)
        button.toggled.connectLambda { showPlace(place) }
        return place
    }

    private fun showPlace(place: Place) {
        currentPlace = place
        for (other in placeList) {
            if (place == other) {
                place.panel.show()
                place.button.setPressedNoSignal(true)
            } else {
                other.panel.hide()
                other.button.setPressedNoSignal(false)
            }
        }
    }

    private fun update() {
        goldCountLabel.text = gameState.gold.toString()
        brewingPanel.update(gameState)

        stockPanel.renderSections {
            renderSection("Ingredients", gameState.ingredients) {
                when (currentPlace) {
                    places.brewingPlace -> addIngredientToCauldron(it)
                }
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
}

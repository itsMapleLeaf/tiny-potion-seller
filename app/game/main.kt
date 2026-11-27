package game

import game.components.BrewingPanel
import game.components.StockPanel
import game.components.StorefrontPanel
import game.data.GameState
import game.data.Ingredient
import game.shared.ensureNodeAs
import game.shared.lazyNode
import godot.annotation.Script
import godot.api.Button
import godot.api.Control
import godot.api.InputEvent
import godot.api.Node
import godot.extension.connectLambda


@Script
class Main : Node() {
    private val gameState = GameState.loadOrCreate()

    private val goldCountLabel by lazyNode<Control>("%GoldCount")
    private val stockPanel by lazyNode<StockPanel>("%StockPanel")
    private val brewingPanel by lazyNode<BrewingPanel>("%BrewingPanel")
    private val storefrontPanel by lazyNode<StorefrontPanel>("%StorefrontPanel")
    private val ingredientStorePanel by lazyNode<Control>("%IngredientStorePanel")
    private val adventureGuildPanel by lazyNode<Control>("%AdventureGuildPanel")
    private val placeButtonsContainer by lazyNode<Control>("%Places")

    // Defer place creation until first use, so we don't access panel nodes before _ready()
    // Create all of them together, so show() can update every place in placeList
    private val places by lazy {
        class Place(val button: Button, val panel: Control)

        object {
            private val all = mutableListOf<Place>()
            var current: Place? = null

            val brewing = definePlace("Brewing", brewingPanel)
            val storefront = definePlace("OpenShop", storefrontPanel)
            val ingredientStore = definePlace("Ingredients", ingredientStorePanel)
            val adventureGuild = definePlace("Adventure", adventureGuildPanel)

            private fun definePlace(name: String, panel: Control): Place {
                val button = placeButtonsContainer.ensureNodeAs<Button>(name)
                val place = Place(button, panel)
                all.add(place)
                button.toggled.connectLambda { show(place) }
                return place
            }

            fun show(place: Place) {
                current = place
                for (other in all) {
                    if (place == other) {
                        place.panel.show()
                        place.button.setPressedNoSignal(true)
                    } else {
                        other.panel.hide()
                        other.button.setPressedNoSignal(false)
                    }
                }
            }
        }
    }

    override fun _ready() {
        brewingPanel.brew.connectLambda { brew() }
        brewingPanel.removeIngredientFromCauldron.connectLambda { removeIngredientFromCauldron(it) }

        storefrontPanel.upsell.connectLambda {
            gameState.update { haggle?.upsell() }
        }

        storefrontPanel.closeDeal.connectLambda {
            gameState.update { haggle?.closeDeal() }
        }

        storefrontPanel.retry.connectLambda {
            gameState.update { haggle?.retry() }
        }

        places.show(places.brewing)

        update()
    }

    override fun _unhandledKeyInput(event: InputEvent) {
        if (event.isActionPressed("debug_quit")) {
            getTree()!!.quit()
        }
    }


    private fun update() {
        goldCountLabel.set("text", gameState.gold.toString())
        brewingPanel.update(gameState)

        stockPanel.renderSections {
            renderSection("Ingredients", gameState.ingredients) {
                when (places.current) {
                    places.brewing -> addIngredientToCauldron(it)
                }
            }

            renderSection("Potions", gameState.potions) { potion ->
                when (places.current) {
                    places.storefront -> {
                        gameState.update {
                            potions.remove(potion)
                            startHaggle(potion)
                        }
                    }
                }
            }
        }

        when (val haggle = gameState.haggle) {
            null -> storefrontPanel.renderPending()
            else -> storefrontPanel.renderHaggle(haggle)
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

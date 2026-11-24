package game

import game.cauldrons.Cauldron
import game.ingredients.Ingredient
import game.inventory.Inventory
import game.potions.Potion


object GameState {
    val dayNumber = 1
    var timeRemaining = 6
    var gold = 0
    val ingredientStock = Inventory<Ingredient>()
    val cauldron = Cauldron.Mudpack()
    val potionStock = Inventory<Potion>()

    init {
        ingredientStock.add(Ingredient.Feyberry, 10)
        ingredientStock.add(Ingredient.MandrakeRoot, 10)
        ingredientStock.add(Ingredient.SackOfSlime, 10)
    }

    fun addIngredientToCauldron(ingredient: Ingredient) {
        ingredientStock.transfer(ingredient, cauldron.ingredients)
    }

    fun removeIngredientFromCauldron(ingredient: Ingredient) {
        cauldron.ingredients.transfer(ingredient, ingredientStock)
    }

    fun brew() {
        val result = cauldron.getBrewResult() ?: return
        potionStock.add(result.potion)
        cauldron.ingredients.clear()
    }
}

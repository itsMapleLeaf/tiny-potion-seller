package game.data


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
        potionStock.add(result.potion, result.count)
        cauldron.ingredients.clear()
    }
}

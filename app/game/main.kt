package game

import godot.annotation.Script
import godot.api.Button
import godot.api.Control
import godot.api.Node
import godot.extension.getNodeAs

@Script
class Main : Node() {
    val ingredientItemTemplate by lazy { getNodeAs<Button>("%IngredientItem")!! }
    val ingredientList by lazy { getNodeAs<Control>("%IngredientList")!! }

    override fun _ready() {
        val minorHealthPotion = PotionSpec(PotionTier.Minor, PotionType.HealthPotion)
        val minorManaPotion = PotionSpec(PotionTier.Minor, PotionType.ManaPotion)

        val gameData = GameData().run {
            ingredients.add(IngredientSpec.Feyberry, 2)
            ingredients.add(IngredientSpec.MandrakeRoot, 2)
            ingredients.add(IngredientSpec.SackOfSlime, 2)

            potions.add(minorHealthPotion, 2)
            potions.add(minorManaPotion, 2)

            for ((ingredient, count) in ingredients) {
                val item = ingredientItemTemplate.duplicate() as Button
                item.text = "${ingredient.name} (${ingredient.magimins.displayText}) x$count"
                ingredientList.addChild(item)
                item.show()
            }
        }

    }
}

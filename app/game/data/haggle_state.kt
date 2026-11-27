package game.data

import game.shared.random
import godot.global.GD
import kotlinx.serialization.Serializable
import kotlin.math.roundToInt

@Serializable
class HaggleState(val potion: Potion) {
    var price = potion.price
    var customerPatience = GD.randiRange(8, 20)

    val isAlive get() = customerPatience > 0

    fun upsell() {
        customerPatience -= patienceLossRange.random()
        if (customerPatience < 1) return

        val increase = price.toDouble() * priceIncreaseRange.random()
        price += increase.roundToInt()
    }

    companion object {
        val priceIncreaseRange = 0.05..0.10
        val patienceLossRange = 2..4
    }
}

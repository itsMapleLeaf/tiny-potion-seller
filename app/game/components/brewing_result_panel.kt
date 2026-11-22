package game.components

import game.data.Magimin
import game.data.MagiminCounts
import game.data.Potion
import game.data.asRatio
import game.data.magiminCounts
import game.data.magiminRatio
import game.extensions.lazyNode
import godot.api.PanelContainer
import godot.annotation.Script
import godot.api.Control
import kotlin.properties.Delegates
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty

data class BrewingResult(
    val magimins: MagiminCounts,
    val potion: Potion,
    val count: Int,
) {
    val magiminRatio get() = magimins.asRatio()
}

@Script
class BrewingResultPanel : PanelContainer() {
    var result: BrewingResult? = null
        set(value) {
            field = value
            update()
        }

    private val contentContainer by lazyNode<Control>("%ContentContainer")
    private val magiminBalance by lazyNode<Control>("%MagiminBalance")
    private val brewingResultPotionSummary by lazyNode<BrewingResultPotionSummary>("%BrewingResultPotionSummary")
    private val pendingStateContainer by lazyNode<Control>("%PendingStateContainer")

    override fun _ready() {
        update()
    }

    fun update() {
        val result = result
        if (result == null) {
            contentContainer.hide()
            pendingStateContainer.show()
            return
        }

        pendingStateContainer.hide()
        contentContainer.show()

        brewingResultPotionSummary.potion = result.potion

        val presentMagimins = result.magimins.asRatio().filter { it != 0.0 }
        val magiminAverage = presentMagimins.sum() / presentMagimins.size
        val potionMagiminRatio = result.potion.kind.magimins.asRatio()

        for ((index, magimin) in Magimin.entries.withIndex()) {
            val meter = magiminBalance.getChild(index) as MagiminMeter
            meter.magimin = magimin
            meter.amount = result.magimins[index]
            meter.ratio = result.magiminRatio[index]
            meter.target = potionMagiminRatio[index]
        }
    }
}

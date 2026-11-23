package game.components

import game.data.BrewingState
import game.data.Magimin
import game.data.asRatio
import game.extensions.lazyNode
import godot.api.PanelContainer
import godot.annotation.Script
import godot.api.Control
import godot.api.Label


@Script
class BrewingResultPanel : PanelContainer() {
    var state: BrewingState = BrewingState.None
        set(value) {
            field = value
            update()
        }

    private val contentContainer by lazyNode<Control>("%ContentContainer")
    private val magiminBalance by lazyNode<Control>("%MagiminBalance")
    private val brewingResultPotionSummary by lazyNode<BrewingResultPotionSummary>("%BrewingResultPotionSummary")
    private val pendingStateContainer by lazyNode<Control>("%PendingStateContainer")
    private val stabilityReportNode by lazyNode<Control>("%StabilityReport")
    private val stabilitySummaryLabel by lazyNode<Label>("%StabilitySummary")
    private val stabilityDetailsLabel by lazyNode<Label>("%StabilityDetails")

    override fun _ready() {
        update()
    }

    fun update() = when (val state = this.state) {
        BrewingState.None -> {
            contentContainer.hide()
            pendingStateContainer.show()
        }

        is BrewingState.BrewedPotion -> {
            pendingStateContainer.hide()
            contentContainer.show()

            brewingResultPotionSummary.potion = state.potion

            val presentMagimins = state.magimins.asRatio().filter { it != 0.0 }
            val magiminAverage = presentMagimins.sum() / presentMagimins.size
            val potionMagiminRatio = state.potion.magimins.asRatio()

            for ((index, magimin) in Magimin.entries.withIndex()) {
                val meter = magiminBalance.getChild(index) as MagiminMeter
                meter.magimin = magimin
                meter.amount = state.magimins[index]
                meter.ratio = state.magimins.asRatio()[index]
                meter.target = potionMagiminRatio[index]
            }

            stabilitySummaryLabel.text = state.stability.summary
            stabilityDetailsLabel.text = state.stability.details
            stabilityReportNode.modulate = state.stability.color

        }
    }
}

package game.components

import game.data.BrewingResult
import game.data.Magimin
import game.data.asRatio
import game.shared.lazyNode
import game.shared.reactive
import godot.annotation.Script
import godot.api.Control
import godot.api.Label
import godot.api.PanelContainer


@Script
class BrewingResultPanel : PanelContainer() {
    var result by reactive<BrewingResult?>(null) { update() }

    private val contentContainer by lazyNode<Control>("%ContentContainer")
    private val magiminBalance by lazyNode<Control>("%MagiminBalance")
    private val brewingResultPotionSummary by lazyNode<BrewingResultPotionSummary>("%BrewingResultPotionSummary")
    private val pendingStateContainer by lazyNode<Control>("%PendingStateContainer")
    private val stabilityReportNode by lazyNode<Control>("%StabilityReport")
    private val stabilitySummaryLabel by lazyNode<Label>("%StabilitySummary")
    private val stabilityDetailsLabel by lazyNode<Label>("%StabilityDetails")
    private val qualityMeter by lazyNode<QualityMeter>("%QualityMeter")

    override fun _ready() {
        update()
    }

    fun update() {
        val result = this.result
        if (result == null) {
            contentContainer.hide()
            pendingStateContainer.show()
            return
        }

        pendingStateContainer.hide()
        contentContainer.show()

        brewingResultPotionSummary.potion = result.potion
        brewingResultPotionSummary.brewedCount = result.count

        qualityMeter.checkpoints = result.potion.tier.magiminCheckpoints
        qualityMeter.value = result.magiminTotal

        val potionMagiminRatio = result.potion.magimins.asRatio()

        for ((index, magimin) in Magimin.entries.withIndex()) {
            val meter = magiminBalance.getChild(index) as MagiminMeter
            meter.magimin = magimin
            meter.amount = result.cauldronMagimins[index]
            meter.ratio = result.cauldronMagimins.asRatio()[index]
            meter.target = potionMagiminRatio[index]
        }

        stabilitySummaryLabel.text = result.stability.summary
        stabilityDetailsLabel.text = result.stability.details
        stabilityReportNode.modulate = result.stability.color
    }
}

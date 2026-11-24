package game.brewing

import game.extensions.lazyNode
import game.extensions.reactive
import game.magimins.Magimin
import game.magimins.MagiminMeter
import game.magimins.asRatio
import game.ui.StarRating
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
    private val starRating by lazyNode<StarRating>("%StarRating")

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

        starRating.filledStarCount = result.potion.rating
        starRating.pendingStarCount = result.stability.chanceBonusStars

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

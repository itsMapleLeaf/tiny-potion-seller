package game.components

import game.data.HaggleState
import game.shared.*
import godot.annotation.Script
import godot.api.Button
import godot.api.Control
import godot.api.Label
import godot.core.signal0
import godot.extension.connectLambda

@Script
class StorefrontPanel : Control() {
    val closeDeal by signal0()
    val upsell by signal0()
    val retry by signal0()

    private val pendingState by lazyNode<Control>("%PendingState")
    private val haggleState by lazyNode<Control>("%HaggleState")
    private val closeDealButton by lazyNode<Button>("%CloseDealButton")
    private val upsellButton by lazyNode<Button>("%UpsellButton")
    private val potionSummary by lazyNode<PotionSummary>("%PotionSummary")
    private val potionPriceCounter by lazyNode<Control>("%PotionPrice")
    private val patienceCounter by lazyNode<Control>("%PatienceCounter")
    private val patronPanel by lazyNode<Control>("%PatronPanel")
    private val haggleLostState by lazyNode<Control>("%HaggleLostState")
    private val tryAgainButton by lazyNode<Button>("%TryAgainButton")
    private val upsellInfo by lazyNode<Label>("%UpsellInfo")

    private val states by lazy { VisibleSwitch(pendingState, haggleState) }

    override fun _ready() {
        closeDealButton.pressed.connectLambda { closeDeal.emit() }
        upsellButton.pressed.connectLambda { upsell.emit() }
        tryAgainButton.pressed.connectLambda { retry.emit() }
        states.showOnly(pendingState)
    }

    fun renderPending() {
        states.showOnly(pendingState)
    }

    fun renderHaggle(haggle: HaggleState) {
        states.showOnly(haggleState)

        patronPanel.visible = haggle.isAlive
        closeDealButton.visible = haggle.isAlive
        haggleLostState.visible = !haggle.isAlive

        potionSummary.potion = haggle.potion
        potionPriceCounter.visible = haggle.isAlive
        potionPriceCounter.set("text", haggle.price.toString())
        patienceCounter.set("text", haggle.customerPatience.toString())

        upsellInfo.text = joinLines(
            "Increase sell price by ${HaggleState.priceIncreaseRange.formatPercentRange()}\n",
            "Decrease patience by ${HaggleState.patienceLossRange.ends().toList().joinToString("-")}",
        )
    }
}

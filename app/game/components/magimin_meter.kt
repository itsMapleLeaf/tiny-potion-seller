package game.components

import game.data.Magimin
import game.shared.lazyNode
import godot.annotation.Export
import godot.annotation.Script
import godot.annotation.Tool
import godot.api.Control
import godot.api.Label

@Script
@Tool
class MagiminMeter : Control() {
    @Export
    var magimin = Magimin.A
        set(value) {
            field = value
            update()
        }

    @Export
    var amount = 0
        set(value) {
            field = value
            update()
        }

    @Export
    var ratio = 0.0
        set(value) {
            field = value
            update()
        }

    @Export
    var target = 0.5
        set(value) {
            field = value
            update()
        }

    private val amountLabel by lazyNode<Label>("%Amount")
    private val meterContainer by lazyNode<Control>("%MeterContainer")
    private val meterFill by lazyNode<Control>("%MeterFill")
    private val meterTargetLine by lazyNode<Control>("%MeterTargetLine")
    private val magiminLabel by lazyNode<Label>("%Magimin")

    override fun _ready() {
        update()
    }

    fun update() {
        amountLabel.text = amount.toString()
        meterFill.modulate = magimin.color
        meterFill.offsetTransformScaleMutate { y = ratio }
        magiminLabel.text = magimin.name

        val hasTarget = target > 0.0 && target < 1.0
        meterTargetLine.visible = hasTarget
        meterTargetLine.offsetTransformPositionMutate {
            y = (1 - target) * meterContainer.size.y
        }

        modulateMutate { a = if (hasTarget) 1.0 else 0.7 }
    }
}

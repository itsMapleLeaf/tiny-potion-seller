package game.components

import game.data.Magimin
import game.extensions.lazyNode
import godot.annotation.Export
import godot.api.VBoxContainer
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

    private val amountLabel by lazyNode<Label>("%Amount")
    private val meterFill by lazyNode<Control>("%MeterFill")
    private val meterCenter by lazyNode<Control>("%MeterCenter")
    private val magiminLabel by lazyNode<Label>("%Magimin")

    override fun _ready() {
        update()
    }

    fun update() {
        amountLabel.text = amount.toString()
        meterFill.modulate = magimin.color
        meterFill.scaleMutate { y = ratio }
        magiminLabel.text = magimin.letter
    }
}

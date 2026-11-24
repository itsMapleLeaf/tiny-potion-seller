package inventory

import extensions.lazyNode
import godot.annotation.Export
import godot.annotation.Script
import godot.api.Button
import godot.api.Control
import godot.api.Label
import godot.core.signal0
import godot.extension.connectLambda

@Script
class ItemTile : Control() {
    val pressed by signal0()

    @Export
    var itemName = "Unknown Item"
        set(value) {
            field = value
            nameLabel.text = value
        }

    @Export
    var count = 1
        set(value) {
            field = value
            countLabel.text = "x $value"
        }

    private val nameLabel by lazyNode<Label>("%NameLabel")
    private val countLabel by lazyNode<Label>("%CountLabel")
    private val button by lazyNode<Button>("%Button")

    override fun _ready() {
        button.pressed.connectLambda { pressed.emit() }
    }
}

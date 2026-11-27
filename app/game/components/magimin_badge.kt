package game.components

import game.data.Magimin
import game.shared.lazyNode
import godot.annotation.Export
import godot.annotation.Script
import godot.annotation.Tool
import godot.api.Control
import godot.api.Label
import kotlin.math.max

@Script
@Tool
class MagiminBadge : Control() {
    @Export
    var magimin = Magimin.A
        set(value) {
            field = value
            update()
        }

    @Export
    var count: Int = 0
        set(value) {
            field = max(value, 0)
            update()
        }

    private val label by lazyNode<Label>("%Label")

    override fun _ready() {
        update()
    }

    private fun update() {
        selfModulate = magimin.color.lightened(0.2)

        label.text = magimin.name
        if (count > 0) label.text += count
    }
}

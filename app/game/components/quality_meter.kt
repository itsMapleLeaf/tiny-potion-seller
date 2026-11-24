package game.components

import game.extensions.ensureNodeAs
import game.extensions.lazyNode
import godot.annotation.Export
import godot.annotation.FloatRange
import godot.annotation.Script
import godot.annotation.Tool
import godot.api.Control
import godot.api.PanelContainer
import godot.common.util.clampValue
import godot.core.Color
import godot.global.GD

@Script
@Tool
class QualityMeter : PanelContainer() {
    @Export
    @FloatRange(0.0, 1.0, 0.01)
    var value = 0.5
        set(value) {
            field = clampValue(value, 0.0, 1.0)
            update()
        }

    private val fill by lazyNode<Control>("%Fill")


    private val tickNodes by lazy {
        ensureNodeAs<Control>("%Ticks")
            .getChildren()
            .filter { it.name.toString().startsWith("Tick") }
            .map { it as Control }
    }

    override fun _ready() {
        update()
    }

    fun update() {
        if (!isNodeReady()) return

        fill.offsetTransformScaleMutate { x = value }

        for ((index, node) in tickNodes.withIndex()) {
            val tickValue = GD.inverseLerp(0.0, tickNodes.size.toDouble() + 1, index + 1.0)
            node.modulate = if (value > tickValue) Color.white else Color.white.darkened(0.6)
        }
    }
}

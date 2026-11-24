package game.components

import game.extensions.*
import godot.annotation.Export
import godot.annotation.Script
import godot.annotation.Tool
import godot.api.Control
import godot.api.Label
import godot.api.PanelContainer
import godot.core.Color

@Script
@Tool
class QualityMeter : PanelContainer() {
    var checkpoints = listOf(0, 10, 20, 30, 40, 50, 60)
        set(value) {
            field = value
            update()
        }


    @Export
    var value = 24
        set(value) {
            field = value
            update()
        }

    private val fill by lazyNode<Control>("%Fill")
    private val valueLabel by lazyNode<Label>("%ValueLabel")

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

        val value = value.clamp(checkpoints.first(), checkpoints.last())

        val currentCheckpointIndex = checkpoints.indexOfLast { value >= it }
        val currentCheckpoint = checkpoints[currentCheckpointIndex]
        val currentCheckpointEnd = checkpoints.getBounded(currentCheckpointIndex + 1)

        val checkpointWeight = currentCheckpointIndex / checkpoints.lastIndex.toDouble()
        val weightWithinCheckpoint = value.weightBetween(currentCheckpoint, currentCheckpointEnd)

        val valueRatio = checkpointWeight + weightWithinCheckpoint / (checkpoints.size - 1)

        fill.offsetTransformScaleMutate { x = valueRatio }
        valueLabel.text = "$value/${checkpoints.last()}"

        for ((index, node) in tickNodes.withIndex()) {
            node.modulate = if ((index + 1) <= currentCheckpointIndex) {
                Color.white
            } else {
                Color.white.darkened(0.6)
            }
        }
    }
}

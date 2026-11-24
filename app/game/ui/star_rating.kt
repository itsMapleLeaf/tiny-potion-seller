package ui

import godot.annotation.Export
import godot.annotation.Script
import godot.api.CompressedTexture2D
import godot.api.HBoxContainer
import godot.api.TextureRect
import godot.api.Time
import godot.common.util.TAU
import godot.common.util.lerp
import godot.global.GD
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

private var filledStar = GD.load<CompressedTexture2D>("res://game/assets/star.svg")!!
private var outlinedStar = GD.load<CompressedTexture2D>("res://game/assets/star-outline.svg")!!

@Script
class StarRating : HBoxContainer() {
    @Export
    var filledStarCount = 0
        set(value) {
            field = max(0, min(5, value))
        }

    @Export
    var pendingStarCount = 0
        set(value) {
            field = max(0, min(5, value))
        }

    override fun _process(delta: Double) {
        for ((index, node) in getChildren().withIndex()) {
            val node = node as TextureRect

            if (index < filledStarCount + pendingStarCount) {
                node.texture = filledStar
            } else {
                node.texture = outlinedStar
            }

            if ((index - filledStarCount) in 0..<pendingStarCount) {
                node.modulateMutate { a = pulse(low = 0.3, high = 1.0, period = 2.0) }
            } else {
                node.modulateMutate { a = 1.0 }
            }
        }
    }
}

fun pulse(low: Double = 0.0, high: Double = 1.0, period: Double = 1.0): Double {
    val raw = sin(Time.getTicksMsec() / 1000.0 * TAU / period)
    val weight = GD.inverseLerp(-1.0, 1.0, raw)
    return lerp(low, high, weight)
}

package game.shared

import godot.global.GD
import kotlin.math.roundToInt
import kotlin.random.Random


fun List<Double>.normalize(): List<Double> {
    val total = sum()
    return map { it / total }
}


fun <T> List<T>.getBounded(index: Int): T =
    get(index.clamp(0, lastIndex))


fun Int.clamp(min: Int, max: Int) = when {
    this < min -> min
    this > max -> max
    else -> this
}

fun Int.weightBetween(min: Int, max: Int): Double =
    GD.inverseLerp(min.toDouble(), max.toDouble(), this.toDouble())


fun Number.asPercent() =
    toDouble().times(100).roundToInt()


fun ClosedFloatingPointRange<Double>.random() =
    Random.nextDouble(start, endInclusive)

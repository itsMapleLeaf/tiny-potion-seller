package game.extensions

import godot.global.GD


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

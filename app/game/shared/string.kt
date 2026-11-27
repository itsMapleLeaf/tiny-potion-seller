package game.shared

fun joinLines(vararg lines: String) =
    lines.joinToString("\n")

fun ClosedFloatingPointRange<Double>.formatPercentRange() =
    "${start.asPercent()}-${endInclusive.asPercent()}%"

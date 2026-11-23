package game.data

import game.extensions.normalize
import godot.core.Color
import kotlin.math.abs


enum class Magimin(val letter: String, val color: Color) {
    A("A", Color("#ff8678")),
    B("B", Color("#78ff8f")),
    C("C", Color("#fff878")),
    D("D", Color("#789cff")),
    E("E", Color("#c278ff")),
}


typealias MagiminCounts = List<Int>
typealias MagiminRatio = List<Double>


val MagiminCounts.a get() = this.getOrNull(0) ?: 0
val MagiminCounts.b get() = this.getOrNull(1) ?: 0
val MagiminCounts.c get() = this.getOrNull(2) ?: 0
val MagiminCounts.d get() = this.getOrNull(3) ?: 0
val MagiminCounts.e get() = this.getOrNull(4) ?: 0


fun magiminCounts(
    a: Int = 0,
    b: Int = 0,
    c: Int = 0,
    d: Int = 0,
    e: Int = 0,
): MagiminCounts = listOf(a, b, c, d, e)


fun magiminRatio(
    a: Double = 0.0,
    b: Double = 0.0,
    c: Double = 0.0,
    d: Double = 0.0,
    e: Double = 0.0,
): MagiminRatio = listOf(a, b, c, d, e).normalize()


val MagiminCounts.displayText: String
    get() = zip("ABCDE".asIterable())
        .filter { (count, _) -> count > 0 }
        .joinToString(", ") { (count, letter) -> letter + count.toString() }


fun MagiminCounts.asRatio(): MagiminRatio =
    map { it.toDouble() }.normalize()


fun MagiminRatio.deviationTo(other: MagiminRatio) =
    zip(other).sumOf { (a, b) -> abs(a - b) }

fun MagiminCounts.ratioDeviationTo(other: MagiminCounts) =
    asRatio().deviationTo(other.asRatio())


fun MagiminCounts.multiplyMagimins(factor: Int): MagiminCounts =
    map { it * factor }


fun MagiminCounts.addMagimins(other: MagiminCounts): MagiminCounts =
    zip(other).map { (a, b) -> a + b }


fun MagiminCounts.subtractMagimins(other: MagiminCounts): MagiminCounts =
    zip(other).map { (a, b) -> a - b }

fun MagiminCounts.isZero() = all { it == 0 }

package game.data

import game.extensions.normalize
import kotlin.math.abs


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
) = listOf(a, b, c, d, e)


fun magiminRatio(
    a: Double = 0.0,
    b: Double = 0.0,
    c: Double = 0.0,
    d: Double = 0.0,
    e: Double = 0.0,
) = listOf(a, b, c, d, e).normalize()

fun magiminRatio(
    a: Int = 0,
    b: Int = 0,
    c: Int = 0,
    d: Int = 0,
    e: Int = 0,
) = magiminCounts(a, b, c, d, e).asRatio()


val MagiminCounts.displayText: String
    get() = zip("ABCDE".asIterable())
        .filter { (count, _) -> count > 0 }
        .joinToString(", ") { (count, letter) -> letter + count.toString() }


fun MagiminCounts.asRatio(): MagiminRatio =
    map { it.toDouble() }.normalize()


fun MagiminRatio.deviationTo(other: MagiminRatio) =
    zip(other).sumOf { (a, b) -> abs(a - b) } / 5

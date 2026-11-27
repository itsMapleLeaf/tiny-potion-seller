package game.shared

fun <T : Comparable<T>> ClosedRange<T>.ends() =
    start to endInclusive

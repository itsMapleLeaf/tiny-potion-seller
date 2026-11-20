package game.extensions


fun List<Double>.normalize(): List<Double> {
    val highest = max()
    return map { it / highest }
}

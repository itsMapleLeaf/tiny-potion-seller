package extensions


fun List<Double>.normalize(): List<Double> {
    val total = sum()
    return map { it / total }
}

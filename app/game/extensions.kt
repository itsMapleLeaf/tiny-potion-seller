package game

fun <T> MutableMap<T, Int>.add(item: T, amount: Int = 1) {
    this[item] = (this[item] ?: 0) + amount
}

fun <T> MutableMap<T, Int>.remove(item: T, amount: Int = 1) {
    var count = this[item] ?: 0
    count -= amount
    if (count > 0) {
        this[item] = count
    } else {
        this.remove(item)
    }
}

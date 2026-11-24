package extensions

typealias CounterMap<T> = MutableMap<T, Int>

fun <T> counterMapOf() =
    mutableMapOf<T, Int>()

fun <T> CounterMap<T>.addCount(item: T, amount: Int = 1) {
    this[item] = (this[item] ?: 0) + amount
}

fun <T> CounterMap<T>.removeCount(item: T, amount: Int = 1) {
    var count = this[item] ?: 0
    count -= amount
    if (count > 0) {
        this[item] = count
    } else {
        this.remove(item)
    }
}

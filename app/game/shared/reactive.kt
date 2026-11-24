package game.shared

import kotlin.reflect.KProperty


class Reactive<T>(private var value: T, private val onSet: (newValue: T) -> Unit) {
    operator fun getValue(instance: Any?, property: KProperty<*>): T {
        return value
    }

    operator fun setValue(instance: Any?, property: KProperty<*>, newValue: T) {
        val changed = value != newValue
        value = newValue
        if (changed) onSet(newValue)
    }
}

fun <T> reactive(initialValue: T, onSet: (newValue: T) -> Unit) =
    Reactive(initialValue, onSet)

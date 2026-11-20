package game.extensions

import godot.api.InstancePlaceholder
import godot.api.Node
import godot.extension.getNodeAs


fun <T : Node> Node.getInstancePlaceholder(path: String): TypedInstancePlaceholder<T> =
    TypedInstancePlaceholder(getNodeAs<InstancePlaceholder>(path)!!)


class TypedInstancePlaceholder<T>(val placeholder: InstancePlaceholder) {
    @Suppress("UNCHECKED_CAST")
    fun createInstance(): T =
        (placeholder.createInstance() ?: error("No instance created")) as T
}

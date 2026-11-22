package game.extensions

import godot.api.InstancePlaceholder
import godot.api.Node
import godot.extension.getNodeAs


fun <T : Node> Node.getInstancePlaceholder(path: String) =
    TypedInstancePlaceholder<T>(ensureNodeAs<InstancePlaceholder>(path))

fun <T : Node> Node.lazyInstancePlaceholder(path: String) =
    lazy { getInstancePlaceholder<T>(path) }


class TypedInstancePlaceholder<T>(val placeholder: InstancePlaceholder) {
    @Suppress("UNCHECKED_CAST")
    fun createInstance(): T =
        (placeholder.createInstance() ?: error("No instance created")) as T
}

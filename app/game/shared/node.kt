package game.shared

import godot.api.Node

inline fun <reified T : Node> Node.ensureNodeAs(nodePath: String): T {
    val node = getNode(nodePath) ?: error("Failed to find node at path \"$nodePath\"")
    return node as? T ?: error(
        "Expected instance of %s, got %s".format(T::class.simpleName, node.getGodotClass())
    )
}

inline fun <reified T : Node> Node.lazyNode(nodePath: String) =
    lazy { ensureNodeAs<T>(nodePath) }

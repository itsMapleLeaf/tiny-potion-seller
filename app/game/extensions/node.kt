package extensions

import godot.api.Node
import godot.extension.getNodeAs

inline fun <reified T : Node> Node.ensureNodeAs(nodePath: String): T {
    val node = getNodeAs<Node>(nodePath) ?: error("Failed to find node at path \"$nodePath\"")
    return node as? T ?: error(
        "Expected instance of %s, got %s".format(T::class.simpleName, node.getGodotClass())
    )
}

inline fun <reified T : Node> Node.lazyNode(nodePath: String) =
    lazy { ensureNodeAs<T>(nodePath) }

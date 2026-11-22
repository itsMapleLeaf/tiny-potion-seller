package game.extensions

import godot.api.Node
import godot.extension.getNodeAs

fun <T : Node> Node.ensureNodeAs(nodePath: String) =
    getNodeAs<T>(nodePath) ?: error("Failed to find node at path \"$nodePath\"")

fun <T : Node> Node.lazyNode(nodePath: String) =
    lazy { ensureNodeAs<T>(nodePath) }

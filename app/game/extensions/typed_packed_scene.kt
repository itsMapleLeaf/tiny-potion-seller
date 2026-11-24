package game.extensions

import godot.api.Node
import godot.api.PackedScene
import godot.global.GD

fun <T : Node> loadSceneAs(path: String) = TypedPackedScene<T>(path)

class TypedPackedScene<T : Node>(private val path: String) {
    private val packedScene = GD.load<PackedScene>(path)
        ?: error("Failed to load packed scene at \"$path\"")

    fun instantiate(): Node {
        val instance = packedScene.instantiate()
            ?: error("Failed to instantiate packed scene at \"$path\"")

        @Suppress("UNCHECKED_CAST")
        return instance
    }
}

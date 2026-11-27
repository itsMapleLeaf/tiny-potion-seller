package game.data

import game.components.ItemTile
import game.shared.loadSceneAs
import godot.api.InstancePlaceholder
import godot.api.Node
import godot.extension.connectLambda
import kotlinx.serialization.Serializable

interface InventoryItem {
    val displayName: String
}

@Serializable
class Inventory<I : InventoryItem> {
    val items: Map<I, Int>
        field = mutableMapOf<I, Int>()

    val size get() = items.values.sum()
    val isEmpty get() = items.isEmpty()

    fun listItems() =
        items.flatMap { (item, count) -> List(count) { item } }

    fun countOf(item: I) =
        items[item] ?: 0

    fun add(item: I, amount: Int = 1) {
        items[item] = countOf(item) + amount
    }

    fun remove(item: I, amount: Int = 1) {
        items[item] = countOf(item) - amount
        if (countOf(item) < 1) items.remove(item)
    }

    fun clear() {
        items.clear()
    }

    fun transfer(item: I, destination: Inventory<I>) {
        remove(item)
        destination.add(item)
    }

    fun renderItemTiles(container: Node, onItemPressed: (I) -> Unit) {
        val itemTileScene = loadSceneAs<ItemTile>("uid://bw7bonlmpmgv")

        for (node in container.getChildren()) {
            if (node !is InstancePlaceholder) {
                node.queueFree()
            }
        }

        for ((item, count) in items.toSortedMap { a, b -> a.displayName.compareTo(b.displayName) }) {
            val itemTile = itemTileScene.instantiate() as ItemTile
            itemTile.itemName = item.displayName
            itemTile.count = count
            itemTile.pressed.connectLambda { onItemPressed(item) }
            container.addChild(itemTile)
        }
    }
}

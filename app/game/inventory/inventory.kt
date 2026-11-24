package inventory

import extensions.loadSceneAs
import godot.api.InstancePlaceholder
import godot.api.Node
import godot.extension.connectLambda


interface InventoryItem {
    val displayName: String
}

class Inventory<I : InventoryItem> {
    private val itemTileScene = loadSceneAs<ItemTile>("res://game/inventory/item_tile.tscn")!!

    val items: List<I>
        field = mutableListOf()

    operator fun iterator() = items.toList()

    val isEmpty get() = items.isEmpty()


    fun add(item: I, amount: Int = 1) {
        for (i in 1..amount) items.add(item)
    }

    fun remove(item: I) {
        items.remove(item)
    }

    fun clear() {
        items.clear()
    }

    fun transfer(item: I, destination: Inventory<I>) {
        remove(item)
        destination.add(item)
    }

    fun recreateItemTiles(container: Node, onItemPressed: (I) -> Unit) {
        for (node in container.getChildren()) {
            if (node !is InstancePlaceholder) {
                node.queueFree()
            }
        }

        val itemCounts = items
            .sortedBy { it.displayName }
            .fold(mutableMapOf<I, Int>()) { counts, item ->
                counts[item] = (counts[item] ?: 0) + 1
                counts
            }

        for ((item, count) in itemCounts) {
            val itemTile = itemTileScene.instantiate() as ItemTile
            itemTile.itemName = item.displayName
            itemTile.count = count
            itemTile.pressed.connectLambda { onItemPressed(item) }
            container.addChild(itemTile)
        }
    }
}


package game.components

import game.data.Inventory
import game.data.InventoryItem
import game.shared.lazyNode
import game.shared.loadSceneAs
import godot.annotation.Script
import godot.api.Control
import godot.api.InstancePlaceholder
import godot.api.Label
import godot.extension.connectLambda

@Script
class StockPanelSection : Control() {
    private val itemTileScene = loadSceneAs<ItemTile>("uid://bw7bonlmpmgv")

    private val titleLabel by lazyNode<Label>("%Title")
    private val itemsContainer by lazyNode<Control>("%Items")

    fun <I : InventoryItem> render(title: String, inventory: Inventory<I>, onItemPressed: (I) -> Unit) {
        titleLabel.text = title

        itemsContainer.getChildren()
            .filter { it !is InstancePlaceholder }
            .forEach { it.queueFree() }

        for ((item, inventoryCount) in inventory.items.toSortedMap { a, b ->
            a.displayName.compareTo(b.displayName)
        }) {
            itemsContainer.addChild(
                itemTileScene.instantiate().apply {
                    itemName = item.displayName
                    count = inventoryCount
                    pressed.connectLambda { onItemPressed(item) }
                }
            )
        }
    }
}

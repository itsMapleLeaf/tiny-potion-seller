package game.components

import game.data.Inventory
import game.data.InventoryItem
import game.shared.clearNonPlaceholderChildren
import game.shared.lazyInstancePlaceholder
import game.shared.lazyNode
import godot.annotation.Script
import godot.api.Control

@Script
class StockPanel : Control() {
    private val sectionPlaceholder by lazyInstancePlaceholder<StockPanelSection>("%StockPanelSection")
    private val sectionsContainer by lazyNode<Control>("%Sections")

    fun renderSections(block: StockPanel.() -> Unit) {
        sectionsContainer.clearNonPlaceholderChildren()
        block()
    }

    fun <I : InventoryItem> renderSection(
        title: String,
        inventory: Inventory<I>,
        onItemPressed: (I) -> Unit
    ) {
        sectionPlaceholder.createInstance().apply {
            render(title, inventory, onItemPressed)
        }
    }
}

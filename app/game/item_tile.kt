package game

import godot.annotation.Export
import godot.annotation.Script
import godot.api.Button
import godot.api.Control
import godot.api.Label
import godot.extension.getNodeAs

@Script
class ItemTile : Control() {
    @Export
    var itemName = "Unknown Item"
        set(value) {
            field = value
            nameLabel.text = value
        }

    @Export
    var count = 1
        set(value) {
            field = value
            countLabel.text = value.toString()
        }

    private val nameLabel by lazy { getNodeAs<Label>("%NameLabel")!! }
    private val countLabel by lazy { getNodeAs<Label>("%CountLabel")!! }
    private val button by lazy { getNodeAs<Button>("%Button")!! }
}

package game.components

import game.data.Magimin
import game.data.Potion
import game.extensions.lazyNode
import godot.annotation.Export
import godot.annotation.Script
import godot.annotation.Tool
import godot.api.Control
import godot.api.Label

@Script
@Tool
class BrewingResultPotionSummary : Control() {
    @Export
    var potion = Potion(Potion.Tier.Minor, Potion.Kind.HealthPotion)
        set(value) {
            field = value
            update()
        }

    private val nameLabel by lazyNode<Label>("%NameLabel")
    private val magiminBadgeGroup by lazyNode<Control>("%MagiminBadgeGroup")

    override fun _ready() {
        update()
    }

    fun update() {
        nameLabel.text = potion.displayName

        for ((index, magimin) in Magimin.entries.withIndex()) {
            var badge = magiminBadgeGroup.getChild(index) as MagiminBadge
            badge.magimin = magimin
            badge.count = potion.kind.magimins[index]
            badge.visible = badge.count > 0
        }
    }
}

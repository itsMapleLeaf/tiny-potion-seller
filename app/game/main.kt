package game

import game.components.BrewingPanel
import game.shared.ensureNodeAs
import game.shared.lazyNode
import godot.annotation.Script
import godot.api.InputEvent
import godot.api.Label
import godot.api.Node
import godot.global.GD

@Script
class Main : Node() {
    private val goldCountLabel by lazyNode<Label>("%GoldCountLabel")

    override fun _ready() {
        GD.print("ready")
        Game.loadGameState()
        goldCountLabel.text = Game.state.gold.toString()
        ensureNodeAs<BrewingPanel>("%BrewingPanel").refresh()
    }

    override fun _unhandledKeyInput(event: InputEvent) {
        if (event.isActionPressed("debug_quit")) {
            getTree()!!.quit()
        }
    }
}

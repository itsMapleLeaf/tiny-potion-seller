package game

import game.data.GameState
import game.shared.lazyNode
import godot.annotation.Script
import godot.api.InputEvent
import godot.api.Label
import godot.api.Node

@Script
class Main : Node() {
    private val goldCountLabel by lazyNode<Label>("%GoldCountLabel")

    override fun _ready() {
        goldCountLabel.text = GameState.gold.toString()
    }

    override fun _unhandledKeyInput(event: InputEvent) {
        if (event.isActionPressed("debug_quit")) {
            getTree()!!.quit()
        }
    }
}

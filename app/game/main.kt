package game

import godot.annotation.Script
import godot.api.InputEvent
import godot.api.Node

@Script
class Main : Node() {
    override fun _unhandledKeyInput(event: InputEvent) {
        if (event.isActionPressed("debug_quit")) {
            getTree()!!.quit()
        }
    }
}

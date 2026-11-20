package game

import godot.annotation.Script
import godot.api.Button
import godot.api.Control
import godot.api.InputEvent
import godot.api.InputEventKey
import godot.api.Node
import godot.extension.getNodeAs

@Script
class Main : Node() {
    override fun _unhandledKeyInput(event: InputEvent) {
        if (event.isActionPressed("debug_quit")) {
            getTree()!!.quit()
        }
    }
}

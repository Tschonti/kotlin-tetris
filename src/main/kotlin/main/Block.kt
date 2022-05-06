package main

import javafx.scene.paint.Color

class Block(var empty: Boolean, var pos: Position, val color: Color = Color.GRAY) {
    companion object {
        const val size = 25.0
    }
}
package main

import javafx.scene.paint.Color

class Block(var pos: Position, val color: Color, var empty: Boolean = false) {
    companion object {
        const val size = 25.0
    }
}
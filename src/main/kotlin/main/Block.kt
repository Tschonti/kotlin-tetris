package main

import javafx.scene.paint.Color

class Block(var pos: Position, var color: Color, var empty: Boolean = false) {
    companion object {
        const val size = 25.0
    }

    fun makeEmpty() {
        color = Constants.GRAY
        empty = true
    }
}
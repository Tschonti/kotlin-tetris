package main

import javafx.scene.paint.Color

fun getResource(filename: String): String {
    return Game::class.java.getResource(filename).toString()
}

class Position(var x: Int, var y: Int)

enum class Direction {
    UP, RIGHT, DOWN, LEFT;

    fun next(): Direction {
        return when(this) {
            UP -> RIGHT
            RIGHT -> DOWN
            DOWN -> LEFT
            LEFT -> UP
        }
    }
}

enum class State {
    MENU, PLAYING, PAUSED, GAMEOVER
}

class Constants {
    companion object {
        const val WIDTH = 10
        const val HEIGHT = 20
        val GRAY = Color(0.75, 0.75, 0.75, 1.0)
        val BLUE = Color(0.0196, 0.0078, 0.67, 1.0)
        val PURPLE = Color(0.4392, 0.0078, 0.67, 1.0)
        val RED = Color(0.67, 0.0078, 0.0078, 1.0)
        val ORANGE = Color(0.8313, 0.5176, 0.0, 1.0)
        val YELLOW = Color(0.9098, 0.83921, 0.08235, 1.0)
        val CYAN = Color(0.2745, 0.83137, 0.8588, 1.0)
        val GREEN = Color(0.01569, 0.67, 0.09411, 1.0)
        val boardOffsetX = 120.0
        val boardOffsetY = 100.0
        val gapBetweenBlocks = 2.0
    }
}

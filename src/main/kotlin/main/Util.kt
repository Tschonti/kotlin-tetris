package main

import javafx.scene.paint.Color
import javafx.scene.text.Font
import kotlinx.serialization.Serializable

class Position(var x: Int, var y: Int)

@Serializable
data class Result(var place: Int, val name: String, val points: Int)

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
        const val boardOffsetX = 120.0
        const val boardOffsetY = 100.0
        const val gapBetweenBlocks = 2.0
        val HUGE_FONT = Font(50.0)
        val BIG_FONT = Font(30.0)
        val SMALL_FONT = Font(10.0)
    }
}

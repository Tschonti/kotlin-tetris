package main

fun getResource(filename: String): String {
    return Game::class.java.getResource(filename).toString()
}

class Position(var x: Int, var y: Int)

enum class Direction {
    UP, RIGHT, DOWN, LEFT
}

class Constants {
    companion object {
        const val WIDTH = 10
        const val HEIGHT = 20
    }
}

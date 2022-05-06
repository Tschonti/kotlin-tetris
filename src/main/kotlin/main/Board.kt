package main

class Board {
    private val board = Array(Constants.HEIGHT) {y -> Array(Constants.WIDTH) { x -> Block(true, Position(x, y))} }

    fun test() {
        for (y: Int in 0 until Constants.HEIGHT) {
            for (x: Int in 0 until Constants.WIDTH) {
                println(board[y][x].pos.x)
                println(board[y][x].pos.y)
            }
        }
    }

    fun get(p: Position): Block {
        return board[p.y][p.x]
    }

    fun swap(p1: Position, p2: Position) {
        val temp = get(p1)
        board[p1.y][p1.x] = get(p2)
        board[p2.y][p2.x] = temp
    }
}
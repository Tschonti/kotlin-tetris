package main

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import pieces.Tetrimino

class Board(private val game: Game) {
    private val board = Array(Constants.HEIGHT) { y -> Array(Constants.WIDTH) { x -> Block(Position(x, y), Color.GRAY, true)} }
    lateinit var activeTetrimino: Tetrimino
    //private var ghost: Piece

    init {
        newTetrimino()
    }

    private fun newTetrimino() {
        activeTetrimino = Tetrimino.generateTetrimino()
        try {
            add(activeTetrimino)
        } catch (e: IllegalStateException) {
            println("Game over!")
        }
    }

    fun get(p: Position): Block {
        return board[p.y][p.x]
    }

    private fun swap(p1: Position, p2: Position) {
        val temp = get(p1)
        board[p1.y][p1.x] = get(p2)
        board[p2.y][p2.x] = temp
    }

    fun remove(p: Tetrimino) {
        p.blocks.forEach { board[it.pos.y][it.pos.x] = Block(Position(it.pos.x, it.pos.y), Color.GRAY, true) }
    }

    fun add(p: Tetrimino) {
        p.blocks.forEach {
            if (!board[it.pos.y][it.pos.x].empty) {
                throw IllegalStateException("There's another block there!")
            }
            board[it.pos.y][it.pos.x] = it }
    }

    fun draw(gc: GraphicsContext) {
        val offsetX = 120.0
        val offsetY = 100.0
        val gap = 2.0
        for (y: Int in 0 until Constants.HEIGHT) {
            for (x: Int in 0 until Constants.WIDTH) {
                val b = board[y][x]
                gc.fill = b.color
                gc.fillRect(offsetX + x * Block.size + x * gap, offsetY + y * Block.size + y * gap, Block.size, Block.size)
            }
        }
    }

    fun placeTetrimino() {
        val diff = blocksFromBottom(activeTetrimino)
        remove(activeTetrimino)
        activeTetrimino.blocks.forEach { it.pos.y += diff }
        add(activeTetrimino)
        clearRows()

        newTetrimino()
    }

    fun blocksFromBottom(t: Tetrimino): Int {
        return t.xToMaxY().minOf { (x, y) ->
            try {
                board.flatten().filter { it.pos.x == x && it.pos.y > y && !it.empty}.minOf {it.pos.y } - y - 1
            } catch (e: NoSuchElementException) {
                Constants.HEIGHT - y - 1
            }
        }
    }

    fun canMoveRight(t: Tetrimino): Boolean {
        return t.yToMaxX().all { (x, y) -> x + 1 < (board[y].firstOrNull { it.pos.x > x && !it.empty }?.pos?.x ?: Constants.WIDTH )}
    }

    fun canMoveLeft(t: Tetrimino): Boolean {
        return t.yToMinX().all { (x, y) -> x - 1 > (board[y].lastOrNull { it.pos.x < x && !it.empty }?.pos?.x ?: -1 )}
    }

    fun leftBound(t: Tetrimino): Int {
        return t.yToMinX().maxOf { (x, y) -> (board[y].lastOrNull { it.pos.x < x && !it.empty }?.pos?.x ?: -1 )}
    }

    fun rightBound(t: Tetrimino): Int {
        return t.yToMaxX().minOf { (x, y) -> (board[y].firstOrNull { it.pos.x > x && !it.empty }?.pos?.x ?: Constants.WIDTH )}
    }

    fun downBound(t: Tetrimino): Int {
        return t.blocks.maxOf {it.pos.y} + blocksFromBottom(t) + 1
    }

    fun upBound(t: Tetrimino): Int {
        return t.xToMinY().maxOf { (x, y) ->
            try {
                board.flatten().filter { it.pos.x == x && it.pos.y < y && !it.empty}.maxOf {it.pos.y }
            } catch (e: NoSuchElementException) {
                -1
            }
        }
    }

    fun step() {
        if (blocksFromBottom(activeTetrimino) > 0) {
            activeTetrimino.moveDown()
        } else {
            clearRows()
            newTetrimino()
        }
    }

    private fun clearRows() {
        val rowsToClear = board.filter { row -> row.all { block -> !block.empty } }
        rowsToClear.forEach { it.forEach { b -> b.makeEmpty() } }
        val rowIndicesToClear = rowsToClear.map { it[0].pos.y }.sortedDescending().toMutableList()
        rowIndicesToClear.forEachIndexed { idx, y ->
            moveRowsDown(y)
            for (y2 in idx + 1 until rowIndicesToClear.size) {
                rowIndicesToClear[y2]++
            }
        }
        game.rowsCleared(rowsToClear.size)
    }

    private fun moveRowsDown(until: Int) {
        for (y in until - 1 downTo  0) {
            for (x in 0 until Constants.WIDTH) {
                if (board[y][x].pos.y == 19) {
                    println()
                }
                swap(board[y][x].pos, Position(board[y][x].pos.x, board[y][x].pos.y + 1))
                board[y + 1][x].pos = Position(x, y + 1)
                board[y][x].pos = Position(x, y)
            }
        }
    }
}
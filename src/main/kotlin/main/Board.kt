package main

import javafx.scene.canvas.GraphicsContext
import pieces.Tetrimino

class Board(private val game: Game) {
    private val board = Array(Constants.HEIGHT) { y -> Array(Constants.WIDTH) { x -> Block(Position(x, y), Constants.GRAY, true)} }
    lateinit var activeTetrimino: Tetrimino
    //private var ghost: Piece

    init {
        newTetrimino()
    }

    /**
     * Replaces the active tetrimino with a new one.
     * If it collides with an existing block, notifies the controller about the game being over.
     */
    private fun newTetrimino() {
        activeTetrimino = Tetrimino.generateTetrimino()
        try {
            add(activeTetrimino)
        } catch (e: IllegalStateException) {
            println("Game over!")
        }
    }

    /**
     * Returns the block in [p]
     */
    fun get(p: Position): Block {
        return board[p.y][p.x]
    }

    /**
     * Swaps the block in [p1] with [p2]
     */
    private fun swap(p1: Position, p2: Position) {
        val temp = get(p1)
        board[p1.y][p1.x] = get(p2)
        board[p2.y][p2.x] = temp
    }

    /**
     * Replaces the blocks of [p] with empty blocks
     */
    fun remove(p: Tetrimino) {
        p.blocks.forEach { board[it.pos.y][it.pos.x] = Block(Position(it.pos.x, it.pos.y), Constants.GRAY, true) }
    }

    /**
     * Adds the blocks of [p] to the board.
     * If there are already blocks in its position, it throws an [IllegalStateException]
     */
    fun add(p: Tetrimino) {
        p.blocks.forEach {
            if (!board[it.pos.y][it.pos.x].empty) {
                throw IllegalStateException("There's another block there!")
            }
            board[it.pos.y][it.pos.x] = it }
    }

    /**
     * Draws the board to the [gc]
     */
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

    /**
     * Places the active tetrimino as low on the board as possible
     */
    fun placeTetrimino() {
        val diff = blocksFromBottom(activeTetrimino)
        remove(activeTetrimino)
        activeTetrimino.blocks.forEach { it.pos.y += diff }
        add(activeTetrimino)
        clearRows()

        newTetrimino()
    }

    /**
     * Calculates how many spaces are there between the lowest point of [t] and the bottom of the board.
     */
    fun blocksFromBottom(t: Tetrimino): Int {
        return t.xToMaxY().minOf { (x, y) ->
            try {
                board.flatten().filter { it.pos.x == x && it.pos.y > y && !it.empty}.minOf {it.pos.y } - y - 1
            } catch (e: NoSuchElementException) {
                Constants.HEIGHT - y - 1
            }
        }
    }

    /**
     * Returns true of [t] can move right
     */
    fun canMoveRight(t: Tetrimino): Boolean {
        return t.yToMaxX().all { (x, y) -> x + 1 < (board[y].firstOrNull { it.pos.x > x && !it.empty }?.pos?.x ?: Constants.WIDTH )}
    }

    /**
     * Returns true of [t] can move left
     */
    fun canMoveLeft(t: Tetrimino): Boolean {
        return t.yToMinX().all { (x, y) -> x - 1 > (board[y].lastOrNull { it.pos.x < x && !it.empty }?.pos?.x ?: -1 )}
    }

    /**
     * Returns the biggest X coord in the range of [t] that's still left of [t]
     */
    fun leftBound(t: Tetrimino): Int {
        return t.yToMinX().maxOf { (x, y) -> (board[y].lastOrNull { it.pos.x < x && !it.empty }?.pos?.x ?: -1 )}
    }

    /**
     * Returns the smallest X coord in the range of [t] that's still right of [t]
     */
    fun rightBound(t: Tetrimino): Int {
        return t.yToMaxX().minOf { (x, y) -> (board[y].firstOrNull { it.pos.x > x && !it.empty }?.pos?.x ?: Constants.WIDTH )}
    }

    /**
     * Returns the smallest Y coord in the range of [t] that's still below [t]
     */
    fun downBound(t: Tetrimino): Int {
        return t.blocks.maxOf {it.pos.y} + blocksFromBottom(t) + 1
    }

    /**
     * Returns the biggest Y coord in the range of [t] that's still above [t]
     */
    fun upBound(t: Tetrimino): Int {
        return t.xToMinY().maxOf { (x, y) ->
            try {
                board.flatten().filter { it.pos.x == x && it.pos.y < y && !it.empty}.maxOf {it.pos.y }
            } catch (e: NoSuchElementException) {
                -1
            }
        }
    }

    /**
     * Moves the active tetrimino down one space, if possible.
     * If not, checks if there are rows to clear and calls for a new tetrimino.
     */
    fun step() {
        if (blocksFromBottom(activeTetrimino) > 0) {
            activeTetrimino.moveDown()
        } else {
            clearRows()
            newTetrimino()
        }
    }

    /**
     * Clears every row of the board that only consists of non-empty blocks.
     */
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

    /**
     * Moves every block in every row until [until] down one space
     */
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
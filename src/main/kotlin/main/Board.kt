package main

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import pieces.Tetrimino

class Board(private val game: Game) {
    // Two-dimensional array of blocks
    private val board = Array(Constants.HEIGHT) { y -> Array(Constants.WIDTH) { x -> Block(Position(x, y), Constants.GRAY, true)} }
    lateinit var activeTetrimino: Tetrimino
    private var nextTetrimino: Tetrimino = Tetrimino.generateTetrimino()
    private var storedTetrimino: Tetrimino? = null
    private var usedStore = false
    private var combo = 0

    init {
        newTetrimino()
    }

    /**
     * Replaces the active tetrimino with the next one and generates a new one to show as the upcoming.
     * If it collides with an existing block, notifies the controller about the game being over.
     */
    private fun newTetrimino() {
        usedStore = false
        activeTetrimino = nextTetrimino
        nextTetrimino = Tetrimino.generateTetrimino()
        try {
            add(activeTetrimino)
        } catch (e: IllegalStateException) {
            game.gameOver()
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
        //draw the board itself
        for (y: Int in 0 until Constants.HEIGHT) {
            for (x: Int in 0 until Constants.WIDTH) {
                val b = board[y][x]
                gc.fill = b.color
                gc.fillRect(Constants.boardOffsetX + x * Block.size + x * Constants.gapBetweenBlocks, Constants.boardOffsetY + y * Block.size + y * Constants.gapBetweenBlocks, Block.size, Block.size)
            }
        }

        //draw the ghost tetrimino
        val diff = blocksFromBottom(activeTetrimino)
        activeTetrimino.blocks.forEach {
            if (board[it.pos.y + diff][it.pos.x].empty) {
                gc.fill = Color(it.color.red, it.color.green, it.color.blue, 0.3)
                gc.fillRect(Constants.boardOffsetX + it.pos.x * Block.size + it.pos.x * Constants.gapBetweenBlocks, Constants.boardOffsetY + (it.pos.y + diff) * Block.size + (it.pos.y + diff) * Constants.gapBetweenBlocks, Block.size, Block.size)
            }
        }

        gc.fill = Constants.PURPLE
        gc.font = Constants.BIG_FONT
        gc.fillText("Next", 415.0, 80.0)
        gc.fillText("Stored", 12.0, 80.0)
        // draw the upcoming tetrimino
        drawRectWithTetrimino(gc, nextTetrimino, 400.0)

        // draw the stored tetrimino
        drawRectWithTetrimino(gc, storedTetrimino, 2.0)
    }

    /**
     * Draws a gray, rounded rectangle from ([startX], [startY]) and [t] in the middle of it
     */
    private fun drawRectWithTetrimino(gc: GraphicsContext, t: Tetrimino?, startX: Double, startY: Double = 100.0) {
        gc.lineWidth = 2.0
        gc.stroke = Constants.GRAY
        gc.strokeRoundRect(startX, startY, 110.0 , 110.0, 5.0, 5.0)
        if (t != null) {
            val startOfT = Position(t.blocks.minOf { it.pos.x }, t.blocks.minOf { it.pos.y })
            val h = (t.rangeY().last - t.rangeY().first + 1) * (Block.size + Constants.gapBetweenBlocks) - Constants.gapBetweenBlocks
            val w = (t.rangeX().last - t.rangeX().first + 1) * (Block.size + Constants.gapBetweenBlocks) - Constants.gapBetweenBlocks
            t.blocks.forEach { b ->
                gc.fill = b.color
                gc.fillRect(startX + 2 + (106 - w) / 2 + (b.pos.x - startOfT.x) * Block.size + (b.pos.x - startOfT.x) * Constants.gapBetweenBlocks, startY + 2 + (106 - h) / 2 + (b.pos.y - startOfT.y) * Block.size + (b.pos.y - startOfT.y) * Constants.gapBetweenBlocks, Block.size, Block.size)
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
     * If it hasn't been used since the current tetrimino appeared,
     * stores the active tetrimino and replaces it with the one in the store or a new one
     */
    fun storeTetrimino() {
        if (!usedStore) {
            usedStore = true
            val temp = storedTetrimino
            remove(activeTetrimino)
            storedTetrimino = activeTetrimino
            storedTetrimino!!.toStartingPos()
            if (temp == null) {
                newTetrimino()
            } else {
                activeTetrimino = temp
            }
        }
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
        game.rowsCleared(rowsToClear.size, combo)
        if (rowsToClear.isNotEmpty()) {
            combo++
        } else {
            combo = 0
        }
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
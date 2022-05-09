package pieces

import javafx.scene.paint.Color
import main.*

abstract class Tetrimino {
    companion object {
        lateinit var board: Board

        /**
         * Generates a random tetrimino
         */
        fun generateTetrimino(): Tetrimino {
            return when (Game.random.nextInt(0, 7)) {
                0 -> Blue()
                1 -> Cyan()
                2 -> Green()
                3 -> Orange()
                4 -> Purple()
                5 -> Red()
                else -> Yellow()
            }
        }
    }
    abstract val size: Int      // max(length on the x axis, length on the y axis)
    var blocks: List<Block>
    abstract val color: Color
    protected var orientation: Direction = Direction.UP
    protected var leftBound = -1
    protected var rightBound = Constants.WIDTH
    protected var downBound = Constants.HEIGHT
    protected var upBound = -1

    init {
        blocks = generateBlocks()
    }

    /**
     * Return the range of the tetrimino's Y coordinates
     */
    fun rangeY(): IntRange {
        return blocks.minOf { it.pos.y }.rangeTo(blocks.maxOf { it.pos.y })
    }

    /**
     * Return the range of the tetrimino's X coordinates
     */
    fun rangeX(): IntRange {
        return blocks.minOf { it.pos.x }.rangeTo(blocks.maxOf { it.pos.x })
    }

    /**
     * Return a list of (X, Y) pairs, where to every X of the tetrimino, it gives the biggest Y in that column
     * Basically the biggest Y in each column
     */
    fun xToMaxY(): List<Pair<Int, Int>> {
        return rangeX().map { x -> Pair(x, blocks.filter { it.pos.x == x }.maxOf { it.pos.y })}
    }

    /**
     * Return a list of (X, Y) pairs, where to every X of the tetrimino, it gives the smallest Y in that column
     * Basically the smallest Y in each column
     */
    fun xToMinY(): List<Pair<Int, Int>> {
        return rangeX().map { x -> Pair(x, blocks.filter { it.pos.x == x }.minOf { it.pos.y })}
    }

    /**
     * Return a list of (X, Y) pairs, where to every Y of the tetrimino, it gives the biggest X in that row
     * Basically the biggest X in each row
     */
    fun yToMaxX(): List<Pair<Int, Int>> {
        return rangeY().map { y -> Pair(blocks.filter { it.pos.y == y }.maxOf { it.pos.x }, y)}
    }

    /**
     * Return a list of (X, Y) pairs, where to every Y of the tetrimino, it gives the smallest X in that row
     * Basically the smallest X in each row
     */
    fun yToMinX(): List<Pair<Int, Int>> {
        return rangeY().map { y -> Pair(blocks.filter { it.pos.y == y }.minOf { it.pos.x }, y)}
    }

    /**
     * Moves the tetrimino down one space, if it's possible
     */
    fun moveDown() {
        if (blocks.maxOf { it.pos.y } < Constants.HEIGHT - 1 && board.blocksFromBottom(this) > 0) {
            board.remove(this)
            blocks.forEach { it.pos.y++ }
            board.add(this)
        }
    }

    /**
     * Moves the tetrimino left one space, if it's possible
     */
    fun moveLeft() {
        if (board.canMoveLeft(this)) {
            board.remove(this)
            blocks.forEach { it.pos.x-- }
            board.add(this)
        }
    }

    /**
     * Moves the tetrimino right one space, if it's possible
     */
    fun moveRight() {
        if (board.canMoveRight(this)) {
            board.remove(this)
            blocks.forEach { it.pos.x++ }
            board.add(this)
        }
    }

    /**
     * Calculates where the tetrimino should start from, assuming it fits between (exclusive) [min] and [max].
     * [coord] is the coordinate (on the same axis as [min] and [max]) of one block of the tetrimino.
     * [fromMin] is how far [coord] is from the smallest coordinate of the tetrimino after rotating.
     * [fromMax] is how far [coord] is from the biggest coordinate of the tetrimino after rotating.
     */
    protected fun baseCoord(coord: Int, min: Int, max: Int, fromMin: Int, fromMax: Int): Int {
        var res = min + 1
        if (coord - fromMin > min) {
            res = if (coord + fromMax < max) {
                coord - fromMin
            } else {
                max - size
            }
        }
        return res
    }

    /**
     * Calculates helper fields for rotating, and checks if the tetrimino can be rotated.
     * For the actual rotation, this method has to be expanded in the non-abstract class
     */
    open fun rotateRight(): Boolean {
        leftBound = board.leftBound(this)
        rightBound = board.rightBound(this)
        downBound = board.downBound(this)
        upBound = board.upBound(this)

        when(orientation) {
            Direction.UP, Direction.DOWN -> {
                if (rightBound - leftBound <= size) {
                    return false
                }
            }
            Direction.LEFT, Direction.RIGHT -> {
                if (downBound - upBound <= size) {
                    return false
                }
            }
        }
        orientation = orientation.next()
        board.remove(this)
        return true
    }

    /**
     * Moves the Tetrimino to it's starting position (top of the board)
     */
    fun toStartingPos() {
        blocks = generateBlocks()
        blocks.forEach { it.color = color }
    }

    /**
     * Generates the blocks of the tetrimino in their starting position.
     * It doesn't set the color.
     */
    protected abstract fun generateBlocks(): List<Block>
}
package pieces

import main.*

abstract class Tetrimino {
    companion object {
        lateinit var board: Board
        fun generateTetrimino(): Tetrimino {
            return when (Game.random.nextInt(0, 4)) {
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
    abstract val size: Int
    abstract val blocks: List<Block>
    protected var orientation: Direction = Direction.UP
    protected var leftBound = -1
    protected var rightBound = Constants.WIDTH
    protected var downBound = Constants.HEIGHT
    protected var upBound = -1

        private fun rangeY(): IntRange {
        return blocks.minOf { it.pos.y }.rangeTo(blocks.maxOf { it.pos.y })
    }

    private fun rangeX(): IntRange {
        return blocks.minOf { it.pos.x }.rangeTo(blocks.maxOf { it.pos.x })
    }

    fun xToMaxY(): List<Pair<Int, Int>> {
        return rangeX().map { x -> Pair(x, blocks.filter { it.pos.x == x }.maxOf { it.pos.y })}
    }

    fun xToMinY(): List<Pair<Int, Int>> {
        return rangeX().map { x -> Pair(x, blocks.filter { it.pos.x == x }.minOf { it.pos.y })}
    }

    fun yToMaxX(): List<Pair<Int, Int>> {
        return rangeY().map { y -> Pair(blocks.filter { it.pos.y == y }.maxOf { it.pos.x }, y)}
    }

    fun yToMinX(): List<Pair<Int, Int>> {
        return rangeY().map { y -> Pair(blocks.filter { it.pos.y == y }.minOf { it.pos.x }, y)}
    }

    fun moveDown() {
        if (blocks.maxOf { it.pos.y } < Constants.HEIGHT - 1 && board.blocksFromBottom(this) > 0) {
            board.remove(this)
            blocks.forEach { it.pos.y++ }
            board.add(this)
        }
    }

    fun moveLeft() {
        if (board.canMoveLeft(this)) {
            board.remove(this)
            blocks.forEach { it.pos.x-- }
            board.add(this)
        }
    }

    fun moveRight() {
        if (board.canMoveRight(this)) {
            board.remove(this)
            blocks.forEach { it.pos.x++ }
            board.add(this)
        }
    }

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

    open fun rotateRight(): Boolean {
        leftBound = board.leftBound(this)
        rightBound = board.rightBound(this)
        downBound = board.downBound(this)
        upBound = board.upBound(this)

        println("left: $leftBound, right: $rightBound, up: $upBound, down: $downBound")

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

    abstract fun rotateLeft()
}
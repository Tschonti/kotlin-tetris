package pieces

import main.*

abstract class Piece {
    companion object {
        lateinit var board: Board
    }
    abstract val blocks: List<Block>
    protected var orientation: Direction = Direction.UP

    fun moveDown() {
        val maxY = blocks.maxOf { it.pos.y }
        val rangeX = blocks.minOf { it.pos.x }.rangeTo(blocks.maxOf { it.pos.x })
        if (maxY < Constants.HEIGHT - 1 && maxY < board.tallestAfter(maxY, rangeX) + 1) {
            board.remove(this)
            blocks.forEach { it.pos.y++ }
            board.add(this)
        }
    }

    fun moveLeft() {
        val minX = blocks.minOf { it.pos.x }
        val rangeY = blocks.minOf { it.pos.y }.rangeTo(blocks.maxOf { it.pos.y })
        if (minX > 0 && minX > board.rightMostBefore(minX, rangeY) + 1) {
            board.remove(this)
            blocks.forEach { it.pos.x-- }
            board.add(this)
        }
    }

    fun moveRight() {
        val maxX = blocks.maxOf { it.pos.x }
        val rangeY = blocks.minOf { it.pos.y }.rangeTo(blocks.maxOf { it.pos.y })
        if (maxX < Constants.WIDTH - 1 && maxX < board.leftMostAfter(maxX, rangeY) - 1) {
            board.remove(this)
            blocks.forEach { it.pos.x++ }
            board.add(this)
        }
    }

    abstract fun rotateRight()
    abstract fun rotateLeft()
}
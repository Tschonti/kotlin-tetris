package pieces

import main.*

abstract class Tetrimino {
    companion object {
        lateinit var board: Board
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

    fun place() {
        val maxY = blocks.maxOf { it.pos.y }
        val newHeight = board.tallestAfter(maxY, blocks.minOf { it.pos.x }.rangeTo(blocks.maxOf { it.pos.x })) - 1
        val diff = newHeight - maxY
        board.remove(this);
        blocks.forEach { it.pos.y += diff }
        board.add(this)
    }

    abstract fun rotateRight()
    abstract fun rotateLeft()
}
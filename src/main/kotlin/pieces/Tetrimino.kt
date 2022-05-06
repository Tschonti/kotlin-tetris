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

    private fun rangeY(): IntRange {
        return blocks.minOf { it.pos.y }.rangeTo(blocks.maxOf { it.pos.y })
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
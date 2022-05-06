package pieces

import main.*

abstract class Piece {
    companion object {
        lateinit var board: Board
    }
    protected abstract val blocks: List<Block>
    protected var orientation: Direction = Direction.UP

    fun moveDown() {
        if (blocks.minOf { it.pos.y } < Constants.HEIGHT - 1) {
            blocks.forEach {
                board.swap(it.pos, Position(it.pos.x, it.pos.y + 1))
                it.pos.y++
            }
        }
    }

    fun moveLeft() {
        if (blocks.minOf { it.pos.x } > 0) {
            blocks.forEach {
                board.swap(it.pos, Position(it.pos.x - 1, it.pos.y))
                it.pos.x--
            }
        }
    }

    fun moveRight() {
        if (blocks.maxOf { it.pos.x } < Constants.WIDTH - 1) {
            blocks.forEach {
                board.swap(it.pos, Position(it.pos.x + 1, it.pos.y))
                it.pos.x++
            }
        }
    }

    abstract fun rotateRight()
    abstract fun rotateLeft()
}
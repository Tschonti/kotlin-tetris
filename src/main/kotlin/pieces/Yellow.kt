package pieces

import main.Block
import main.Constants
import main.Position

class Yellow : Tetrimino() {
    override val size: Int = 2
    override val blocks: List<Block> = listOf(
        Block(Position(4, 0), Constants.YELLOW),
        Block(Position(5, 0), Constants.YELLOW),
        Block(Position(4, 1), Constants.YELLOW),
        Block(Position(5, 1), Constants.YELLOW),
    )

    override fun rotateRight(): Boolean { return true}

    override fun rotateLeft() {}
}
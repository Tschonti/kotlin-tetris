package pieces

import javafx.scene.paint.Color
import main.Block
import main.Position

class Yellow : Tetrimino() {
    override val size: Int = 2
    override val blocks: List<Block> = listOf(
        Block(Position(4, 0), Color.YELLOW),
        Block(Position(5, 0), Color.YELLOW),
        Block(Position(4, 1), Color.YELLOW),
        Block(Position(5, 1), Color.YELLOW),
    )

    override fun rotateRight(): Boolean { return true}

    override fun rotateLeft() {}
}
package pieces

import javafx.scene.paint.Color
import main.Block
import main.Position

class Yellow : Piece() {
    override val blocks: List<Block> = listOf(
        Block(false, Position(4, 0), Color.YELLOW),
        Block(false, Position(5, 0), Color.YELLOW),
        Block(false, Position(4, 1), Color.YELLOW),
        Block(false, Position(5, 1), Color.YELLOW),
    )

    override fun rotateRight() {

    }

    override fun rotateLeft() {

    }
}
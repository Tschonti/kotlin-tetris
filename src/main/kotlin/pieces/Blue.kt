package pieces

import javafx.scene.paint.Color
import main.Block
import main.Position

class Blue : Tetrimino() {
    override val blocks: List<Block> = listOf(
        Block(Position(5, 0), Color.BLUE),
        Block(Position(5, 1), Color.BLUE),
        Block(Position(5, 2), Color.BLUE),
        Block(Position(4, 2), Color.BLUE),
    )

    override fun rotateRight() {
        TODO("Not yet implemented")
    }

    override fun rotateLeft() {
        TODO("Not yet implemented")
    }
}
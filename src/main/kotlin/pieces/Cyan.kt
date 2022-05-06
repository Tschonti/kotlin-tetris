package pieces

import javafx.scene.paint.Color
import main.Block
import main.Position

class Cyan : Tetrimino() {
    override val blocks: List<Block> = listOf(
        Block(Position(5, 1), Color.CYAN),
        Block(Position(5, 0), Color.CYAN),
        Block(Position(5, 2), Color.CYAN),
        Block(Position(5, 3), Color.CYAN),
    )

    override fun rotateRight() {
        TODO("Not yet implemented")
    }

    override fun rotateLeft() {
        TODO("Not yet implemented")
    }
}
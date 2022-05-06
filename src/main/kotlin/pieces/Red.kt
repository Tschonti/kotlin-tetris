package pieces

import javafx.scene.paint.Color
import main.Block
import main.Position

class Red : Tetrimino() {
    override val blocks: List<Block> = listOf(
        Block(Position(5, 0), Color.RED),
        Block(Position(5, 1), Color.RED),
        Block(Position(4, 1), Color.RED),
        Block(Position(4, 2), Color.RED),
    )

    override fun rotateRight() {
        TODO("Not yet implemented")
    }

    override fun rotateLeft() {
        TODO("Not yet implemented")
    }
}
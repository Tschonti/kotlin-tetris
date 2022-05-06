package pieces

import javafx.scene.paint.Color
import main.Block
import main.Position

class Orange : Tetrimino() {
    override val blocks: List<Block> = listOf(
        Block(Position(4, 0), Color.ORANGE),
        Block(Position(4, 1), Color.ORANGE),
        Block(Position(4, 2), Color.ORANGE),
        Block(Position(5, 2), Color.ORANGE),
    )

    override fun rotateRight() {
        TODO("Not yet implemented")
    }

    override fun rotateLeft() {
        TODO("Not yet implemented")
    }
}
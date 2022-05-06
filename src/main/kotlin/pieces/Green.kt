package pieces

import javafx.scene.paint.Color
import main.Block
import main.Position

class Green : Tetrimino() {
    override val blocks: List<Block> = listOf(
        Block(Position(4, 0), Color.GREEN),
        Block(Position(4, 1), Color.GREEN),
        Block(Position(5, 1), Color.GREEN),
        Block(Position(5, 2), Color.GREEN),
    )

    override fun rotateRight() {
        TODO("Not yet implemented")
    }

    override fun rotateLeft() {
        TODO("Not yet implemented")
    }
}
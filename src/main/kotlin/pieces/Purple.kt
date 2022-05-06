package pieces

import javafx.scene.paint.Color
import main.Block
import main.Position

class Purple : Tetrimino() {
    override val blocks: List<Block> = listOf(
        Block(Position(5, 0), Color.PURPLE),
        Block(Position(4, 1), Color.PURPLE),
        Block(Position(5, 1), Color.PURPLE),
        Block(Position(6, 1), Color.PURPLE),
    )

    override fun rotateRight() {
        TODO("Not yet implemented")
    }

    override fun rotateLeft() {
        TODO("Not yet implemented")
    }
}
package pieces

import javafx.scene.paint.Color
import main.Block
import main.Constants
import main.Position

class Yellow : Tetrimino() {
    override val size: Int = 2
    override val color: Color = Constants.YELLOW
    init {
        blocks.forEach { it.color = color }
    }

    override fun rotateRight(): Boolean { return true}

    override fun rotateLeft() {}
    override fun generateBlocks(): List<Block> {
        return listOf(
            Block(Position(4, 0)),
            Block(Position(5, 0)),
            Block(Position(4, 1)),
            Block(Position(5, 1)),
        )
    }
}
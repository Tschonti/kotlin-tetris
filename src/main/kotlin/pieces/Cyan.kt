package pieces

import javafx.scene.paint.Color
import main.Block
import main.Constants
import main.Direction
import main.Position

class Cyan : Tetrimino() {
    override val size: Int = 4
    override val color: Color = Constants.CYAN
    init {
        blocks.forEach { it.color = color }
    }

    override fun rotateRight(): Boolean {
        if (super.rotateRight()) {
            when (orientation) {
                Direction.RIGHT -> {
                    val baseX = baseCoord(blocks[0].pos.x, leftBound, rightBound, 2, 1)
                    blocks.forEachIndexed { idx, b -> b.pos = Position(baseX + idx, blocks[2].pos.y) }
                }
                Direction.DOWN -> {
                    val baseY = baseCoord(blocks[0].pos.y, upBound, downBound, 2, 1)
                    blocks.forEachIndexed { idx, b -> b.pos = Position(blocks[1].pos.x, baseY + idx) }
                }
                Direction.LEFT -> {
                    val baseX = baseCoord(blocks[0].pos.x, leftBound, rightBound, 1, 2)
                    blocks.forEachIndexed { idx, b -> b.pos = Position(baseX + idx, blocks[1].pos.y) }
                }
                Direction.UP -> {
                    val baseY = baseCoord(blocks[0].pos.y, upBound, downBound, 1, 2)
                    blocks.forEachIndexed { idx, b -> b.pos = Position(blocks[2].pos.x, baseY + idx) }
                }
            }
            board.add(this)
        }
        return true
    }

    override fun rotateLeft() {
        TODO("Not yet implemented")
    }

    override fun generateBlocks(): List<Block> {
        return listOf(
            Block(Position(5, 0)),
            Block(Position(5, 1)),
            Block(Position(5, 2)),
            Block(Position(5, 3)),
        )
    }
}
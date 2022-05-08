package pieces

import javafx.scene.paint.Color
import main.Block
import main.Constants
import main.Direction
import main.Position

class Green : Tetrimino() {
    override val size: Int = 3
    override val color: Color = Constants.GREEN
    init {
        blocks.forEach { it.color = color }
    }

    override fun rotateRight(): Boolean {
        if (super.rotateRight()) {
            when (orientation) {
                Direction.RIGHT -> {
                    val baseX = baseCoord(blocks[0].pos.x, leftBound, rightBound, 0, 2)
                    blocks[0].pos = Position(baseX + 2, blocks[0].pos.y + 1)
                    blocks[1].pos = Position(baseX + 1, blocks[1].pos.y)
                    blocks[2].pos = Position(baseX + 1, blocks[2].pos.y + 1)
                    blocks[3].pos = Position(baseX, blocks[3].pos.y)
                }
                Direction.DOWN -> {
                    val baseY = baseCoord(blocks[0].pos.y, upBound, downBound, 1, 1)
                    blocks[0].pos = Position(blocks[0].pos.x - 1, baseY + 2)
                    blocks[1].pos = Position(blocks[1].pos.x, baseY + 1)
                    blocks[2].pos = Position(blocks[2].pos.x - 1, baseY + 1)
                    blocks[3].pos = Position(blocks[3].pos.x, baseY)
                }
                Direction.LEFT -> {
                    val baseX = baseCoord(blocks[0].pos.x, leftBound, rightBound, 2, 0)
                    blocks[0].pos = Position(baseX, blocks[0].pos.y)
                    blocks[1].pos = Position(baseX + 1, blocks[1].pos.y + 1)
                    blocks[2].pos = Position(baseX + 1, blocks[2].pos.y)
                    blocks[3].pos = Position(baseX + 2, blocks[3].pos.y + 1)
                }
                Direction.UP -> {
                    val baseY = baseCoord(blocks[0].pos.y, upBound, downBound, 2, 0)
                    blocks[0].pos = Position(blocks[0].pos.x + 1, baseY)
                    blocks[1].pos = Position(blocks[1].pos.x, baseY + 1)
                    blocks[2].pos = Position(blocks[2].pos.x + 1, baseY + 1)
                    blocks[3].pos = Position(blocks[3].pos.x, baseY + 2)
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
            Block(Position(4, 0)),
            Block(Position(4, 1)),
            Block(Position(5, 1)),
            Block(Position(5, 2)),
        )
    }
}
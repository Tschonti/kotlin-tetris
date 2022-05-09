package pieces

import javafx.scene.paint.Color
import main.Block
import main.Constants
import main.Direction
import main.Position

class Orange : Tetrimino() {
    override val size: Int = 3
    override val color: Color = Constants.ORANGE
    init {
        blocks.forEach { it.color = color }
    }

    override fun rotateRight(): Boolean {
        if(super.rotateRight()) {
            when (orientation) {
                Direction.RIGHT -> {
                    val baseX = baseCoord(blocks[0].pos.x, leftBound, rightBound, 1, 1)
                    blocks[0].pos = Position(baseX, blocks[0].pos.y)
                    for (i in 0 until 3) {
                        blocks[i + 1].pos = Position(baseX + i, blocks[0].pos.y - 1)
                    }
                }
                Direction.DOWN -> {
                    val baseY = baseCoord(blocks[0].pos.y, upBound, downBound, 2, 0)
                    blocks[0].pos = Position(blocks[0].pos.x, baseY)
                    for (i in 0 until 3) {
                        blocks[i + 1].pos = Position(blocks[0].pos.x + 1, baseY + i)
                    }
                }
                Direction.LEFT -> {
                    val baseX = baseCoord(blocks[0].pos.x, leftBound, rightBound, 0, 2)
                    blocks[0].pos = Position(baseX + 2, blocks[0].pos.y)
                    for (i in 0 until 3) {
                        blocks[i + 1].pos = Position(baseX + i, blocks[0].pos.y + 1)
                    }
                }
                Direction.UP -> {
                    val baseY = baseCoord(blocks[0].pos.y, upBound, downBound, 0, 2)
                    blocks[0].pos = Position(blocks[0].pos.x - 1, baseY + 2)
                    for (i in 0 until 3) {
                        blocks[i + 1].pos = Position(blocks[0].pos.x - 1, baseY + i)
                    }
                }
            }
            board.add(this)
        }
        return true
    }

    override fun generateBlocks(): List<Block> {
        return listOf(
            Block(Position(5, 2)),
            Block(Position(4, 0)),
            Block(Position(4, 1)),
            Block(Position(4, 2)),
        )
    }
}
package pieces

import javafx.scene.paint.Color
import main.Block
import main.Constants
import main.Direction
import main.Position

class Purple : Tetrimino() {
    override val size: Int = 3
    override val color: Color = Constants.PURPLE
    init {
        blocks.forEach { it.color = color }
    }

    override fun rotateRight():Boolean {
        if (super.rotateRight()) {
            when (orientation) {
                Direction.RIGHT -> {
                    val baseY = baseCoord(blocks[0].pos.y, upBound, downBound, 0, 2)
                    blocks[0].pos = Position(blocks[0].pos.x + 1, baseY + 1)
                    for (i in 0 until 3) {
                        blocks[i + 1].pos = Position(blocks[0].pos.x - 1, baseY + i)
                    }
                }
                Direction.DOWN -> {
                    val baseX = baseCoord(blocks[0].pos.x, leftBound, rightBound, 2, 0)
                    blocks[0].pos = Position(baseX + 1, blocks[0].pos.y + 1)
                    for (i in 0 until 3) {
                        blocks[i + 1].pos = Position(baseX + i, blocks[0].pos.y - 1)
                    }
                }
                Direction.LEFT -> {
                    val baseY = baseCoord(blocks[0].pos.y, upBound, downBound, 2, 0)
                    blocks[0].pos = Position(blocks[0].pos.x - 1, baseY + 1)
                    for (i in 0 until 3) {
                        blocks[i + 1].pos = Position(blocks[0].pos.x + 1, baseY + i)
                    }
                }
                Direction.UP -> {
                    val baseX = baseCoord(blocks[0].pos.x, leftBound, rightBound, 0, 2)
                    blocks[0].pos = Position(baseX + 1, blocks[0].pos.y - 1)
                    for (i in 0 until 3) {
                        blocks[i + 1].pos = Position(baseX + i, blocks[0].pos.y + 1)
                    }
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
            Block(Position(4, 1)),
            Block(Position(5, 1)),
            Block(Position(6, 1)),
        )
    }
}
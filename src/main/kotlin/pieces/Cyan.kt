package pieces

import javafx.scene.paint.Color
import main.Block
import main.Direction
import main.Position

class Cyan : Tetrimino() {
    override val size: Int = 4
    override val blocks: List<Block> = listOf(
        Block(Position(5, 0), Color.CYAN),
        Block(Position(5, 1), Color.CYAN),
        Block(Position(5, 2), Color.CYAN),
        Block(Position(5, 3), Color.CYAN),
    )

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
}
package main

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import pieces.Tetrimino
import pieces.Yellow

class Board {
    private val board = Array(Constants.HEIGHT) { y -> Array(Constants.WIDTH) { x -> Block(Position(x, y), Color.GRAY, true)} }
    var activeTetrimino: Tetrimino = Tetrimino.generateTetrimino()
    //private var ghost: Piece

    init {
        activeTetrimino.blocks.forEach {
            board[it.pos.y][it.pos.x] = it
        }
    }

    fun get(p: Position): Block {
        return board[p.y][p.x]
    }

    fun swap(p1: Position, p2: Position) {
        val temp = get(p1)
        board[p1.y][p1.x] = get(p2)
        board[p2.y][p2.x] = temp
    }

    fun remove(p: Tetrimino) {
        p.blocks.forEach { board[it.pos.y][it.pos.x] = Block(it.pos, Color.GRAY, true) }
    }

    fun add(p: Tetrimino) {
        p.blocks.forEach { board[it.pos.y][it.pos.x] = it }

    }

    fun draw(gc: GraphicsContext) {
        val offsetX = 120.0
        val offsetY = 100.0
        val gap = 2.0
        for (y: Int in 0 until Constants.HEIGHT) {
            for (x: Int in 0 until Constants.WIDTH) {
                val b = board[y][x]
                gc.fill = b.color
                gc.fillRect(offsetX + x * Block.size + x * gap, offsetY + y * Block.size + y * gap, Block.size, Block.size)
            }
        }
    }

    fun placePiece() {
        activeTetrimino.place()
        activeTetrimino = Tetrimino.generateTetrimino()
        add(activeTetrimino)
    }

    fun leftMostAfter(afterX: Int, rangeY: IntRange): Int {
        return board.copyOfRange(rangeY.first, rangeY.last + 1).minOf { row -> row.firstOrNull { block -> block.pos.x > afterX && !block.empty }?.pos?.x ?: Constants.WIDTH }
    }

    fun rightMostBefore(beforeX: Int, rangeY: IntRange): Int {
        return board.copyOfRange(rangeY.first, rangeY.last + 1).maxOf { row -> row.lastOrNull { block -> block.pos.x < beforeX && !block.empty }?.pos?.x ?: -1 }
    }

    fun tallestAfter(afterY: Int, rangex: IntRange): Int {
        return try {
            board.flatten().filter { b -> b.pos.x in rangex && b.pos.y > afterY && !b.empty }.minOf { it.pos.y }
        } catch (e: NoSuchElementException) {
            Constants.HEIGHT
        }
    }

}
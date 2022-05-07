package main

import javafx.animation.AnimationTimer
import javafx.application.Application
import javafx.event.EventHandler
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.input.KeyCode
import javafx.scene.paint.Color
import javafx.stage.Stage
import pieces.Tetrimino
import kotlin.random.Random

class Game : Application() {

    companion object {
        private const val WIDTH = 512
        private const val HEIGHT = 720
        val random = Random(System.currentTimeMillis())
    }

    private lateinit var mainScene: Scene
    private lateinit var graphicsContext: GraphicsContext
    private val board: Board = Board(this)
    private var interval = 650

    private var lastFrameTime: Long = System.nanoTime()
    private var sinceInterval: Long = 0


    override fun start(mainStage: Stage) {
        mainStage.title = "Tetris"

        val root = Group()
        mainScene = Scene(root)
        mainStage.scene = mainScene

        val canvas = Canvas(WIDTH.toDouble(), HEIGHT.toDouble())
        root.children.add(canvas)

        prepareActionHandlers()

        graphicsContext = canvas.graphicsContext2D

        loadGraphics()

        // Main loop
        object : AnimationTimer() {
            override fun handle(currentNanoTime: Long) {
                tickAndRender(currentNanoTime)
            }
        }.start()

        mainStage.show()
        Tetrimino.board = board
    }

    private fun prepareActionHandlers() {
        mainScene.onKeyPressed = EventHandler { event ->
            when(event.code) {
                KeyCode.RIGHT -> board.activeTetrimino.moveRight()
                KeyCode.DOWN -> board.activeTetrimino.moveDown()
                KeyCode.LEFT -> board.activeTetrimino.moveLeft()
                KeyCode.SPACE -> board.placeTetrimino()
                KeyCode.UP -> board.activeTetrimino.rotateRight()
                else -> {}
            }
        }
    }

    private fun loadGraphics() {

    }

    private fun tickAndRender(currentNanoTime: Long) {
        // the time elapsed since the last frame, in nanoseconds
        // can be used for physics calculation, etc
        val elapsedNanos = currentNanoTime - lastFrameTime
        lastFrameTime = currentNanoTime

        // clear canvas
        graphicsContext.clearRect(0.0, 0.0, WIDTH.toDouble(), HEIGHT.toDouble())

        // perform world updates
        board.draw(graphicsContext)

        // display crude fps counter
        val elapsedMs = elapsedNanos / 1_000_000
        sinceInterval += elapsedMs

        if (sinceInterval > interval) {
            sinceInterval -= interval
            board.step()
        }
        if (elapsedMs != 0L) {
            graphicsContext.fill = Color.BLACK
            graphicsContext.fillText("${1000 / elapsedMs} fps", 10.0, 10.0)
        }
    }

    fun rowsCleared(count: Int) {
        if (count > 0) {
            println("$count row(s) cleared!")
        }
    }

}

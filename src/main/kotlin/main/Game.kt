package main

import javafx.animation.AnimationTimer
import javafx.application.Application
import javafx.event.EventHandler
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.image.Image
import javafx.scene.input.KeyCode
import javafx.scene.paint.Color
import javafx.stage.Stage
import pieces.Piece

class Game : Application() {

    companion object {
        private const val WIDTH = 512
        private const val HEIGHT = 720
    }

    private lateinit var mainScene: Scene
    private lateinit var graphicsContext: GraphicsContext
    private val board: Board = Board()

    private var lastFrameTime: Long = System.nanoTime()

    // use a set so duplicates are not possible
    private val currentlyActiveKeys = mutableSetOf<KeyCode>()

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
        Piece.board = board
    }

    private fun prepareActionHandlers() {
        mainScene.onKeyPressed = EventHandler { event ->
            when(event.code) {
                KeyCode.RIGHT -> board.activePiece.moveRight()
                KeyCode.DOWN -> board.activePiece.moveDown()
                KeyCode.LEFT -> board.activePiece.moveLeft()
                else -> {}
            }
        }
        mainScene.onKeyReleased = EventHandler { event ->
            currentlyActiveKeys.remove(event.code)
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

        // draw background
        //graphicsContext.drawImage(space, 0.0, 0.0)

        // perform world updates
        board.draw(graphicsContext)
        graphicsContext.fill = Color.GRAY

        // draw sun
        //graphicsContext.drawImage(sun, sunX.toDouble(), sunY.toDouble())

        // display crude fps counter
        val elapsedMs = elapsedNanos / 1_000_000
        if (elapsedMs != 0L) {
            graphicsContext.fill = Color.WHITE
            graphicsContext.fillText("${1000 / elapsedMs} fps", 10.0, 10.0)
        }
    }


}

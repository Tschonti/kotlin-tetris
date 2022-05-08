package main

import javafx.animation.AnimationTimer
import javafx.application.Application
import javafx.event.EventHandler
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.Button
import javafx.scene.input.KeyCode
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.stage.Stage
import pieces.Tetrimino
import kotlin.random.Random
import kotlin.system.exitProcess

class Game : Application() {

    companion object {
        private const val WIDTH = 512
        private const val HEIGHT = 720
        val random = Random(System.currentTimeMillis())
    }

    private lateinit var mainScene: Scene
    private lateinit var graphicsContext: GraphicsContext
    private lateinit var board: Board
    private var interval = 650

    private var lastFrameTime: Long = System.nanoTime()
    private var sinceInterval: Long = 0

    private var state = State.MENU
    private var score = 0
    private var scored = 0

    override fun start(mainStage: Stage) {
        mainStage.title = "Tetris"

        // game scene
        val mainRoot = Group()
        mainScene = Scene(mainRoot)
        val canvas = Canvas(WIDTH.toDouble(), HEIGHT.toDouble())
        mainRoot.children.add(canvas)

        // menu scene
        val menuRoot = Group()
        val menuScene = Scene(menuRoot)
        val menuCanvas = Canvas(WIDTH.toDouble(), HEIGHT.toDouble())
        menuRoot.children.add(menuCanvas)
        mainStage.scene = menuScene

        val startButton = Button("Start game")
        menuRoot.children.add(startButton)
        startButton.onMouseClicked = EventHandler {
            state = State.PLAYING
            mainStage.scene = mainScene
            board = Board(this)
            Tetrimino.board = board
            prepareActionHandlers()
        }
        /*val quitButton = Button("Quit game")
        menuRoot.children.add(quitButton)
        quitButton.onMouseClicked = EventHandler {
            exitProcess(0)
        }*/


        graphicsContext = canvas.graphicsContext2D

        // Main loop
        object : AnimationTimer() {
            override fun handle(currentNanoTime: Long) {
                tickAndRender(currentNanoTime)
            }
        }.start()

        mainStage.show()
    }

    fun gameOver() {
        state = State.GAMEOVER
    }

    private fun prepareActionHandlers() {
        mainScene.onKeyPressed = EventHandler { event ->
            when (state) {
                State.PLAYING -> {
                    when (event.code) {
                        KeyCode.RIGHT -> board.activeTetrimino.moveRight()
                        KeyCode.DOWN -> board.activeTetrimino.moveDown()
                        KeyCode.LEFT -> board.activeTetrimino.moveLeft()
                        KeyCode.SPACE -> board.placeTetrimino()
                        KeyCode.UP -> board.activeTetrimino.rotateRight()
                        KeyCode.SHIFT -> board.storeTetrimino()
                        KeyCode.ENTER -> state = State.PAUSED
                        else -> {}
                    }
                }
                State.PAUSED -> {
                    if (event.code == KeyCode.ENTER) {
                        state = State.PLAYING
                    }
                }
                else -> {}
            }
        }
    }

    private fun tickAndRender(currentNanoTime: Long) {
        // the time elapsed since the last frame, in nanoseconds
        // can be used for physics calculation, etc
        val elapsedNanos = currentNanoTime - lastFrameTime
        lastFrameTime = currentNanoTime

        // clear canvas
        graphicsContext.clearRect(0.0, 0.0, WIDTH.toDouble(), HEIGHT.toDouble())

        val elapsedMs = elapsedNanos / 1_000_000
        if (state != State.MENU) {
            graphicsContext.font = Constants.HUGE_FONT
            graphicsContext.fill = Constants.PURPLE
            graphicsContext.fillText("TETRIS", 180.0, 50.0)

            // perform world updates
            board.draw(graphicsContext)

            graphicsContext.font = Constants.BIG_FONT
            graphicsContext.fill = Constants.GRAY
            graphicsContext.fillText("Score: $score", 2.0, 700.0)
            if (scored > 0) {
                graphicsContext.fillText("+$scored", 400.0, 700.0)
            }

            if (state == State.GAMEOVER) {
                graphicsContext.fill = Constants.RED
                graphicsContext.fillText("Game over!", 175.0, 700.0)
            }

            if (state == State.PLAYING) {
                sinceInterval += elapsedMs
                if (sinceInterval > interval) {
                    sinceInterval -= interval
                    board.step()
                }
            }
        }
        // display crude fps counter
        if (elapsedMs != 0L) {
            graphicsContext.font = Constants.SMALL_FONT
            graphicsContext.fill = Color.BLACK
            graphicsContext.fillText("${1000 / elapsedMs} fps", 10.0, 10.0)
        }
    }

    fun rowsCleared(rows: Int, combo: Int) {
        if (rows > 0) {
            scored = ((rows - 1) * 200 + if (rows == 4) 200 else 100) * (combo + 1)
            score += scored
            println("Cleard $rows row(s) with x${combo+1} combo, scored $scored, new score: $score")
        } else {
            scored = 0
        }
    }

}

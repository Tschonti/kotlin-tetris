package main

import javafx.animation.AnimationTimer
import javafx.application.Application
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.Button
import javafx.scene.control.TextInputDialog
import javafx.scene.input.KeyCode
import javafx.scene.layout.BorderPane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.text.Text
import javafx.scene.text.TextFlow
import javafx.stage.Stage
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import pieces.Tetrimino
import java.io.File
import kotlin.random.Random
import kotlin.system.exitProcess

class Game : Application() {
    companion object {
        private const val WIDTH = 512
        private const val HEIGHT = 720
        val random = Random(System.currentTimeMillis())
    }

    private lateinit var mainStage: Stage
    private lateinit var leaderboard: MutableList<Result>
    private lateinit var mainScene: Scene
    private lateinit var menuScene: Scene
    private lateinit var graphicsContext: GraphicsContext
    private val textDialog = TextInputDialog()
    private lateinit var leaderboardVBox: VBox

    private lateinit var board: Board
    private var interval = 700
    private var lastThousand = 0

    private var lastFrameTime: Long = System.nanoTime()
    private var sinceInterval: Long = 0

    private var state = State.MENU
    private var score = 0
    private var scored = 0

    override fun start(mainStage: Stage) {
        this.mainStage = mainStage
        mainStage.title = "Tetris"
        // game scene
        val mainRoot = Group()
        mainScene = Scene(mainRoot)
        val canvas = Canvas(WIDTH.toDouble(), HEIGHT.toDouble())
        mainRoot.children.add(canvas)
        textDialog.headerText = "Congrats, you earned a place on the leaderboard! Please enter your name"
        textDialog.title = "Enter your name"

        // menu scene
        val menuRoot = Group()
        menuScene = Scene(menuRoot)
        val menuCanvas = Canvas(WIDTH.toDouble(), HEIGHT.toDouble())
        menuRoot.children.add(menuCanvas)
        val bp = BorderPane()
        bp.padding = Insets(20.0, 50.0, 20.0, 50.0)
        menuRoot.children.add(bp)
        val startButton = Button("Start game")
        val quitButton = Button("Quit game")
        val title = Text("Tetris")
        title.font = Constants.HUGE_FONT
        val vbox = VBox()
        vbox.spacing = 10.0
        vbox.padding = Insets(0.0, 0.0, 20.0, 0.0)
        vbox.alignment = Pos.BASELINE_CENTER
        vbox.children.addAll(title, startButton, quitButton)
        bp.top = vbox
        this.mainStage.scene = menuScene

        //menuRoot.children.add(startButton)
        startButton.onMouseClicked = EventHandler {
            state = State.PLAYING
            mainStage.scene = mainScene
            board = Board(this)
            Tetrimino.board = board
            score = 0
            scored = 0
            interval = 700
            lastThousand = 0
            sinceInterval = 0
            prepareActionHandlers()
        }
        quitButton.onMouseClicked = EventHandler {
            exitProcess(0)
        }

        //leaderboard
        leaderboard = Json.decodeFromString(File("leaderboard.json").readText())
        leaderboardVBox = VBox()
        fillLeaderboard()

        // how to play
        val howToPlayTitle = Text("How to play")
        howToPlayTitle.font = Constants.BIG_FONT
        val howToPlayVBox = VBox()
        howToPlayVBox.children.addAll(howToPlayTitle, Text("Standar Tetris game. Move the Tetrimino with the right,\nleft and down arrow keys, rotate it with the up arrow, \ndrop it with space, store it with shift.\nYou can pause/resume the game with Enter. \nGood luck!"))
        howToPlayVBox.padding = Insets(20.0, 0.0, 80.0, 0.0)

        bp.center = VBox(leaderboardVBox, howToPlayVBox)
        val creditText = Text("Developed by: Sámuel Fekete\nGitHub username: Tschonti")
        creditText.font = Constants.BIG_FONT
        bp.bottom = creditText
        graphicsContext = canvas.graphicsContext2D

        // Main loop
        object : AnimationTimer() {
            override fun handle(currentNanoTime: Long) {
                tickAndRender(currentNanoTime)
            }
        }.start()

        mainStage.show()
    }

    private fun fillLeaderboard() {
        leaderboardVBox.children.clear()
        val leaderboardTitle = Text("Leaderboard")
        leaderboardTitle.font = Constants.BIG_FONT
        val results: List<String> = leaderboard.map { "${it.place + 1}. ${it.name} ${it.points}" }

        leaderboardVBox.children.add(leaderboardTitle)
        results.forEach { leaderboardVBox.children.add(Text(it)) }

    }

    fun gameOver() {
        state = State.GAMEOVER
        var place = leaderboard.size
        var found = false
        leaderboard.forEach {
            if (!found) {
                if (score >= it.points) {
                    place = it.place
                    found = true
                    if (score > it.points) {
                        it.place++
                    }
                }
            } else {
                if (score > it.points) {
                    it.place++
                }
            }
        }
        if (place < 10 || leaderboard.size < 10) {
            textDialog.contentText = ""
            textDialog.show()
            textDialog.onHidden = EventHandler {
                if (textDialog.editor.text.isNotEmpty()) {
                    leaderboard.add(place, Result(place, textDialog.editor.text, score))
                    leaderboard = leaderboard.subList(0, 10)
                    val f = File("leaderboard.json")
                    f.bufferedWriter().use {
                        val json = Json.encodeToString(leaderboard)
                        it.write(json)
                    }
                    fillLeaderboard()
                }
                state = State.MENU
                mainStage.scene = menuScene
            }
        }
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
        val elapsedNanos = currentNanoTime - lastFrameTime
        lastFrameTime = currentNanoTime
        if (state == State.PLAYING || state == State.PAUSED) {
            val elapsedMs = elapsedNanos / 1_000_000
            graphicsContext.clearRect(0.0, 0.0, WIDTH.toDouble(), HEIGHT.toDouble())
            graphicsContext.font = Constants.HUGE_FONT
            graphicsContext.fill = Constants.PURPLE
            graphicsContext.fillText("TETRIS", 180.0, 50.0)

            // perform world updates
            board.draw(graphicsContext)

            graphicsContext.font = Constants.BIG_FONT
            graphicsContext.fill = Constants.PURPLE
            graphicsContext.fillText("Score: $score", 2.0, 700.0)
            if (scored > 0) {
                graphicsContext.fillText("+$scored", 400.0, 700.0)
            }

            if (state == State.PAUSED) {
                graphicsContext.fill = Constants.RED
                graphicsContext.fillRect(220.0, 300.0, 40.0, 100.0)
                graphicsContext.fillRect(280.0, 300.0, 40.0, 100.0)
                graphicsContext.font = Constants.BIG_FONT
                graphicsContext.fillText("Paused", 220.0, 440.0)
                graphicsContext.font = Constants.SMALL_FONT
                graphicsContext.fillText("Resume with Enter", 230.0, 460.0)

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
    }

    fun rowsCleared(rows: Int, combo: Int) {
        if (rows > 0) {
            scored = ((rows - 1) * 200 + if (rows == 4) 200 else 100) * (combo + 1)
            score += scored
            println("Cleard $rows row(s) with x${combo+1} combo, scored $scored, new score: $score")

            if ((score - lastThousand) > 1000) {
                lastThousand += 1000
                interval -= 35
            }
        } else {
            scored = 0
        }
    }

}

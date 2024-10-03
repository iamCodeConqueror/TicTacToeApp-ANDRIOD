package com.example.tictactoe

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private val board = arrayOfNulls<String>(9)
    private var currentPlayer = "X"
    private var gameActive = true

    private lateinit var buttons: Array<Button>
    private lateinit var playerTurnTextView: TextView
    private lateinit var player1ScoreTextView: TextView
    private lateinit var tiesScoreTextView: TextView
    private lateinit var player2ScoreTextView: TextView

    private var player1Score = 0
    private var tiesScore = 0
    private var player2Score = 0

    private lateinit var player1Name: String
    private lateinit var player2Name: String

    // Boolean flag to track if the game is vs Computer
    private var isVsComputer = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttons = arrayOf(
            findViewById(R.id.button0),
            findViewById(R.id.button1),
            findViewById(R.id.button2),
            findViewById(R.id.button3),
            findViewById(R.id.button4),
            findViewById(R.id.button5),
            findViewById(R.id.button6),
            findViewById(R.id.button7),
            findViewById(R.id.button8)
        )

        playerTurnTextView = findViewById(R.id.textView2)
        player1ScoreTextView = findViewById(R.id.textView4)
        tiesScoreTextView = findViewById(R.id.textView6)
        player2ScoreTextView = findViewById(R.id.textView8)

        player1Name = intent.getStringExtra("PLAYER_1_NAME") ?: "Player 1"
        player2Name = intent.getStringExtra("PLAYER_2_NAME") ?: "Player 2"

        // Check if the game is vs Computer
        isVsComputer = player2Name == "Computer"

        val player1TextView = findViewById<TextView>(R.id.textView3)
        val player2TextView = findViewById<TextView>(R.id.textView7)

        player1Name = toTitleCase(player1Name)
        player2Name = toTitleCase(player2Name)
        player1TextView.text = player1Name
        player2TextView.text = player2Name

        updatePlayerTurn()
        initializeBoard()
    }

    private fun initializeBoard() {
        for (i in buttons.indices) {
            buttons[i].setOnClickListener {
                if (gameActive && board[i].isNullOrEmpty()) {
                    makeMove(buttons[i], i)
                }
            }
        }
    }

    private fun makeMove(button: Button, position: Int) {
        button.text = currentPlayer
        board[position] = currentPlayer

        val winningPositions = checkWinner()
        if (winningPositions != null) {
            gameActive = false
            highlightWinningButtons(winningPositions)
            if (currentPlayer == "X") {
                player1Score++
                player1ScoreTextView.text = player1Score.toString()
                val toast = Toast.makeText(this, "$player1Name wins!", Toast.LENGTH_LONG)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
            } else {
                player2Score++
                player2ScoreTextView.text = player2Score.toString()
                val toast = Toast.makeText(this, "$player2Name wins!", Toast.LENGTH_LONG)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
            }
        } else if (board.all { it != null }) {
            gameActive = false
            tiesScore++
            tiesScoreTextView.text = tiesScore.toString()
            Toast.makeText(this, "It's a draw!", Toast.LENGTH_LONG).show()
        } else {
            currentPlayer = if (currentPlayer == "X") "O" else "X"
            updatePlayerTurn()

            // If it's vs Computer and it's the computer's turn, make a random move
            if (isVsComputer && currentPlayer == "O") {
                makeComputerMove()
            }
        }
    }

    private fun makeComputerMove() {
        // Find an empty spot and place the computer's move there
        val availablePositions = board.indices.filter { board[it].isNullOrEmpty() }
        if (availablePositions.isNotEmpty()) {
            val randomPosition = availablePositions[Random.nextInt(availablePositions.size)]
            buttons[randomPosition].performClick() // Simulate button click for computer's move
        }
    }

    private fun updatePlayerTurn() {
        if (currentPlayer == "X") {
            playerTurnTextView.text = "$player1Name's turn"
        } else {
            playerTurnTextView.text = "$player2Name's turn"
        }
    }

    private fun checkWinner(): IntArray? {
        val winningPositions = arrayOf(
            intArrayOf(0, 1, 2), intArrayOf(3, 4, 5), intArrayOf(6, 7, 8),
            intArrayOf(0, 3, 6), intArrayOf(1, 4, 7), intArrayOf(2, 5, 8),
            intArrayOf(0, 4, 8), intArrayOf(2, 4, 6)
        )

        for (positions in winningPositions) {
            if (board[positions[0]] == board[positions[1]] &&
                board[positions[1]] == board[positions[2]] &&
                board[positions[0]] != null) {
                return positions
            }
        }
        return null
    }

    private fun highlightWinningButtons(winningPositions: IntArray) {
        for (position in winningPositions) {
            buttons[position].setBackgroundColor(ContextCompat.getColor(this, R.color.textcolor))
        }
    }

    fun restartGame(view: View) {
        for (i in buttons.indices) {
            buttons[i].text = ""
            buttons[i].setBackgroundColor(ContextCompat.getColor(this, R.color.teal))
            board[i] = null
        }

        currentPlayer = "X"
        gameActive = true
        updatePlayerTurn()
    }

    fun toTitleCase(text: String): String {
        return text.split(" ").joinToString(" ") { word ->
            word.lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        }
    }
}

package com.example.tictactoe

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    // Array to track the state of the board (null means empty)
    private val board = arrayOfNulls<String>(9)

    // Variable to track the current player ("X" or "O")
    private var currentPlayer = "X"
    // Boolean to track if the game is still active
    private var gameActive = true

    // Declare the buttons for the 3x3 grid
    private lateinit var buttons: Array<Button>

    // Declare the TextViews for score and turn display
    private lateinit var playerTurnTextView: TextView
    private lateinit var player1ScoreTextView: TextView
    private lateinit var tiesScoreTextView: TextView
    private lateinit var player2ScoreTextView: TextView

    // Variables to track the scores
    private var player1Score = 0
    private var tiesScore = 0
    private var player2Score = 0

    // Player names as class-level variables
    private lateinit var player1Name: String
    private lateinit var player2Name: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the buttons and set click listeners
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

        // Initialize the TextViews for scores and turn display
        playerTurnTextView = findViewById(R.id.textView2)
        player1ScoreTextView = findViewById(R.id.textView4)
        tiesScoreTextView = findViewById(R.id.textView6)
        player2ScoreTextView = findViewById(R.id.textView8)

        // Get the player names from the intent
        player1Name = intent.getStringExtra("PLAYER_1_NAME") ?: "Player 1"
        player2Name = intent.getStringExtra("PLAYER_2_NAME") ?: "Player 2"

        // Update the TextViews with the player names
        val player1TextView = findViewById<TextView>(R.id.textView3)
        val player2TextView = findViewById<TextView>(R.id.textView7)

        player1Name = toTitleCase(player1Name)
        player2Name = toTitleCase(player2Name)
        player1TextView.text = player1Name
        player2TextView.text = player2Name

        updatePlayerTurn()
        initializeBoard()
    }

    // This function sets click listeners for each button in the grid
    private fun initializeBoard() {
        for (i in buttons.indices) {
            buttons[i].setOnClickListener {
                // Make a move if the game is active and the clicked button is empty
                if (gameActive && board[i].isNullOrEmpty()) {
                    makeMove(buttons[i], i)
                }
            }
        }
    }

    // Function to make a move on the board
    private fun makeMove(button: Button, position: Int) {
        // Set the button's text to the current player's symbol
        button.text = currentPlayer
        // Update the board state with the current player's symbol
        board[position] = currentPlayer

        // Check if there's a winner and get winning positions if any
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
            // If all positions are filled and no winner, it's a draw
            gameActive = false
            tiesScore++
            tiesScoreTextView.text = tiesScore.toString()
            Toast.makeText(this, "It's a draw!", Toast.LENGTH_LONG).show()
        } else {
            // Switch to the other player and update the turn display
            currentPlayer = if (currentPlayer == "X") "O" else "X"
            updatePlayerTurn()
        }
    }

    // Function to update the turn display with player names
    private fun updatePlayerTurn() {
        if (currentPlayer == "X") {
            playerTurnTextView.text = "$player1Name's turn"
        } else {
            playerTurnTextView.text = "$player2Name's turn"
        }
    }

    // Function to check if a player has won and return winning positions if any
    private fun checkWinner(): IntArray? {
        // Winning combinations
        val winningPositions = arrayOf(
            intArrayOf(0, 1, 2), intArrayOf(3, 4, 5), intArrayOf(6, 7, 8), // Rows
            intArrayOf(0, 3, 6), intArrayOf(1, 4, 7), intArrayOf(2, 5, 8), // Columns
            intArrayOf(0, 4, 8), intArrayOf(2, 4, 6)  // Diagonals
        )

        // Check if any winning combination is met
        for (positions in winningPositions) {
            if (board[positions[0]] == board[positions[1]] &&
                board[positions[1]] == board[positions[2]] &&
                board[positions[0]] != null) {
                return positions // Return the winning positions
            }
        }
        return null // No winner
    }

    // Function to highlight winning buttons based on their positions
    private fun highlightWinningButtons(winningPositions: IntArray) {
        for (position in winningPositions) {
            buttons[position].setBackgroundColor(ContextCompat.getColor(this, R.color.textcolor))
        }
    }

    // Function to restart the game when the restart button is clicked
    fun restartGame(view: View) {
        // Clear the board and reset buttons
        for (i in buttons.indices) {
            buttons[i].text = ""
            buttons[i].setBackgroundColor(ContextCompat.getColor(this, R.color.teal)) // Reset background color.
            board[i] = null
        }

        // Reset the game state variables
        currentPlayer = "X"
        gameActive = true
        updatePlayerTurn() // Reset the turn display
    }
    fun toTitleCase(text: String): String {
        return text.split(" ").joinToString(" ") { word ->
            word.lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        }
    }

}

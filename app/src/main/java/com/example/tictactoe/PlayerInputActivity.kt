package com.example.tictactoe

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class PlayerInputActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_input)

        val player1NameEditText = findViewById<EditText>(R.id.player1_name)
        val player2NameEditText = findViewById<EditText>(R.id.player2_name)
        val startGameButton = findViewById<Button>(R.id.start_button)

        // ImageViews for social media icons
        val linkedInImageView = findViewById<ImageView>(R.id.linkedin)
        val instagramImageView = findViewById<ImageView>(R.id.instagram)
        val twitterImageView = findViewById<ImageView>(R.id.twitter)

        // Start game button listener
        startGameButton.setOnClickListener {
            val player1Name = player1NameEditText.text.toString()
            val player2Name = player2NameEditText.text.toString()

            // Create an intent to pass player names to MainActivity
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("PLAYER_1_NAME", player1Name)
            intent.putExtra("PLAYER_2_NAME", player2Name)
            startActivity(intent)
        }

        // Set click listener for LinkedIn ImageView
        linkedInImageView.setOnClickListener {
            val linkedInUrl = "https://www.linkedin.com/in/mohit-dwivedi13/"
            openUrl(linkedInUrl)
        }

        // Set click listener for Instagram ImageView
        instagramImageView.setOnClickListener {
            val instagramUrl = "https://www.instagram.com/dmohit13/"
            openUrl(instagramUrl)
        }

        // Set click listener for Twitter ImageView
        twitterImageView.setOnClickListener {
            val twitterUrl = "https://twitter.com/dmohit013"
            openUrl(twitterUrl)
        }
    }

    // Function to open URL in a web browser
    private fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }
}

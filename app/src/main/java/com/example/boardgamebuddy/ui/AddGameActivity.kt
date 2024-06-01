package com.example.boardgamebuddy.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.boardgamebuddy.R
import com.example.boardgamebuddy.data.BoardGame
import com.google.firebase.firestore.FirebaseFirestore

class AddGameActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var nameEditText: EditText
    private lateinit var maxPlayersEditText: EditText
    private lateinit var designerEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var imageUrlEditText: EditText
    private lateinit var addButton: Button
    private var nextId: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_game)

        db = FirebaseFirestore.getInstance()

        nameEditText = findViewById(R.id.edit_text_name)
        maxPlayersEditText = findViewById(R.id.edit_text_max_players)
        designerEditText = findViewById(R.id.edit_text_designer)
        descriptionEditText = findViewById(R.id.edit_text_description)
        imageUrlEditText = findViewById(R.id.edit_text_image_url)
        addButton = findViewById(R.id.button_add_game)

        // Get the next available ID for the new board game
        getNextId()

        addButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val maxPlayers = maxPlayersEditText.text.toString().toIntOrNull() ?: 0
            val designer = designerEditText.text.toString()
            val description = descriptionEditText.text.toString()
            val imageUrl = imageUrlEditText.text.toString()

            if (name.isEmpty() || maxPlayers == 0 || designer.isEmpty() || description.isEmpty() || imageUrl.isEmpty()) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            } else {
                val newBoardGame = BoardGame(nextId, name, maxPlayers, designer, description, imageUrl)
                addGameToDatabase(newBoardGame)
            }
        }
    }

    private fun getNextId() {
        db.collection("BoardGames")
            .orderBy("id", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val highestGame = documents.first().toObject(BoardGame::class.java)
                    nextId = highestGame.id + 1
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error fetching highest ID: $exception", Toast.LENGTH_SHORT).show()
            }
    }

    private fun addGameToDatabase(game: BoardGame) {
        // Add the game to Firestore with a custom document ID (the same as the game ID)
        db.collection("BoardGames")
            .document(game.id.toString()) // Set the document ID explicitly
            .set(game)
            .addOnSuccessListener {
                Toast.makeText(this, "Game added successfully", Toast.LENGTH_SHORT).show()
                // Set the result and finish the activity
                setResult(RESULT_OK)
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error adding game: $e", Toast.LENGTH_SHORT).show()
            }
    }
}

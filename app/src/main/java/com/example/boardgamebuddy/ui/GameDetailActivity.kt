package com.example.boardgamebuddy.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.example.boardgamebuddy.R
import com.example.boardgamebuddy.data.BoardGame
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class GameDetailActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private var isFavorite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_detail)

        // Initialize Firebase Authentication
        auth = FirebaseAuth.getInstance()
        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance()

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val navView = findViewById<NavigationView>(R.id.nav_view)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            drawerLayout.closeDrawers()
            when (menuItem.itemId) {
                R.id.nav_login -> {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }

        // Retrieve the game ID from the intent
        val gameId = intent.getIntExtra("GAME_ID", -1)

        if (gameId != -1) {
            fetchBoardGame(gameId)
            setupFavoriteButton(gameId)
        } else {
            Toast.makeText(this, "Error loading game details", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchBoardGame(id: Int) {
        db.collection("BoardGames").document(id.toString())
            .get()
            .addOnSuccessListener { document ->
                val game = document.toObject(BoardGame::class.java)
                if (game != null) {
                    updateUI(game)
                } else {
                    Toast.makeText(this, "Game not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error getting game details: $exception", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateUI(game: BoardGame) {
        val detailImage = findViewById<ImageView>(R.id.detailImage)
        val detailDescription = findViewById<TextView>(R.id.detailDescription)
        supportActionBar?.title = game.name
        detailDescription.text = game.description
        Glide.with(this).load(game.imageUrl).into(detailImage)
    }

    private fun setupFavoriteButton(gameId: Int) {
        val fabFavorite = findViewById<FloatingActionButton>(R.id.fab)

        // Check if the game is already a favorite
        val currentUser = auth.currentUser
        if (currentUser != null) {
            db.collection("users").document(currentUser.uid)
                .collection("favorites").document(gameId.toString())
                .get()
                .addOnSuccessListener { document ->
                    isFavorite = document.exists()
                    updateFavoriteButton(fabFavorite)
                }
        }

        fabFavorite.setOnClickListener {
            if (currentUser != null) {
                val favoritesRef = db.collection("users").document(currentUser.uid)
                    .collection("favorites").document(gameId.toString())
                if (isFavorite) {
                    favoritesRef.delete()
                        .addOnSuccessListener {
                            isFavorite = false
                            updateFavoriteButton(fabFavorite)
                            Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Failed to remove from favorites: $e", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    favoritesRef.set(hashMapOf("gameId" to gameId))
                        .addOnSuccessListener {
                            isFavorite = true
                            updateFavoriteButton(fabFavorite)
                            Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Failed to add to favorites: $e", Toast.LENGTH_SHORT).show()
                        }
                }
            } else {
                Toast.makeText(this, "Please log in to use this feature", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateFavoriteButton(fabFavorite: FloatingActionButton) {
        if (isFavorite) {
            fabFavorite.setImageResource(R.drawable.ic_favorite)
        } else {
            fabFavorite.setImageResource(R.drawable.ic_favorite_border)
        }
    }
}

package com.example.boardgamebuddy.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.example.boardgamebuddy.R
import com.example.boardgamebuddy.data.MockData
import com.example.boardgamebuddy.data.BoardGame
import com.google.android.material.navigation.NavigationView

class GameDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_detail)

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
                    val intent: Intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }

        // Retrieve the game ID from the intent
        val gameId = intent.getIntExtra("GAME_ID", -1)

        // Use the game ID to fetch game details from your data source
        val game = MockData.games.find { it.id == gameId }

        // Update the views with game details
        val detailImage = findViewById<ImageView>(R.id.detailImage)
        val detailDescription = findViewById<TextView>(R.id.detailDescription)

        if (game != null) {
            supportActionBar?.title = game.name

            detailDescription.text = game.description
            Glide.with(this).load(game.imageUrl).into(detailImage)
        }
    }
}

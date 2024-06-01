package com.example.boardgamebuddy.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.example.boardgamebuddy.R
import com.example.boardgamebuddy.adapters.GameAdapter
import com.example.boardgamebuddy.data.BoardGame
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var listView: RecyclerView
    private lateinit var userEmailTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase Authentication
        auth = FirebaseAuth.getInstance()
        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance()

        // Setup toolbar
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Games"

        // Setup drawer and navigation view
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        // Setup header view
        val headerView: View = navView.getHeaderView(0)
        userEmailTextView = headerView.findViewById(R.id.user_email)
        updateHeader(auth.currentUser)

        // Setup RecyclerView
        listView = findViewById(R.id.gamesRecyclerView)
        listView.layoutManager = GridLayoutManager(this, 1) // Assuming you want a grid layout
        fetchBoardGames()

        // Handle drawer toggle
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Handle navigation item clicks
        navView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            drawerLayout.closeDrawers()
            when (menuItem.itemId) {
                R.id.nav_login -> startActivity(Intent(this, LoginActivity::class.java))
                R.id.nav_logout -> {
                    auth.signOut()
                    drawerLayout.closeDrawer(navView)
                    recreate()
                }
                R.id.nav_your_games -> startActivity(Intent(this, YourGamesActivity::class.java))
            }
            true
        }

        // Update drawer menu based on login status
        updateDrawerMenu(auth.currentUser != null)
    }

    override fun onStart() {
        super.onStart()
        updateDrawerMenu(auth.currentUser != null)
    }

    private fun updateHeader(user: FirebaseUser?) {
        if (user != null) {
            userEmailTextView.text = user.email
            userEmailTextView.visibility = View.VISIBLE
        } else {
            userEmailTextView.visibility = View.GONE
        }
    }

    private fun updateDrawerMenu(isLoggedIn: Boolean) {
        val menuRes = if (isLoggedIn) R.menu.menu_logged_in else R.menu.menu_logged_out
        navView.menu.clear()
        navView.inflateMenu(menuRes)
    }

    private fun fetchBoardGames() {
        db.collection("BoardGames")
            .get()
            .addOnSuccessListener { result ->
                val games = result.map { document -> document.toObject(BoardGame::class.java) }
                setupRecyclerView(games)
            }
            .addOnFailureListener { exception ->
                println("Error getting documents: $exception")
            }
    }

    private fun setupRecyclerView(games: List<BoardGame>) {
        val adapter = GameAdapter(games)
        listView.adapter = adapter
        adapter.setOnClickListener(object : GameAdapter.OnClickListener {
            override fun onClick(position: Int, model: BoardGame) {
                val intent = Intent(this@MainActivity, GameDetailActivity::class.java).apply {
                    putExtra("GAME_ID", model.id)
                }
                startActivity(intent)
            }
        })
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(navView)) {
            drawerLayout.closeDrawer(navView)
        } else {
            super.onBackPressed()
        }
    }
}

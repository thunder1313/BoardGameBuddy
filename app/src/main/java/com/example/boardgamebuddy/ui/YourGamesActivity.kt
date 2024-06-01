package com.example.boardgamebuddy.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.boardgamebuddy.R
import com.example.boardgamebuddy.adapters.GameAdapter
import com.example.boardgamebuddy.adapters.PagerAdapter
import com.example.boardgamebuddy.data.BoardGame
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class YourGamesActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_your_games)

        // Initialize Firebase Authentication
        auth = FirebaseAuth.getInstance()
        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance()

        val pagerAdapter = PagerAdapter(supportFragmentManager)
        val pager = findViewById<ViewPager>(R.id.pager)
        if (pager != null) {
            pager.adapter = pagerAdapter
            val tabLayout = findViewById<TabLayout>(R.id.tabs)
            tabLayout.setupWithViewPager(pager)
            tabLayout.getTabAt(0)?.select()
        }
    }
}
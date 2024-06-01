package com.example.boardgamebuddy.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.boardgamebuddy.R
import com.example.boardgamebuddy.adapters.GameAdapter
import com.example.boardgamebuddy.data.BoardGame
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FavoritesFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var listView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase Authentication
        auth = FirebaseAuth.getInstance()
        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favorites, container, false)

        // Setup RecyclerView
        listView = view.findViewById(R.id.favoritesRecyclerView)
        listView.layoutManager = GridLayoutManager(context, 1)
        fetchFavoriteGames()

        return view
    }

    private fun fetchFavoriteGames() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            db.collection("users").document(currentUser.uid)
                .collection("favorites")
                .get()
                .addOnSuccessListener { result ->
                    val favoriteGameIds = result.map { document -> document.getLong("gameId")?.toInt() ?: -1 }
                    if (favoriteGameIds.isNotEmpty()) {
                        fetchBoardGames(favoriteGameIds)
                    } else {
                        Toast.makeText(requireContext(), "No favorite games found", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(requireContext(), "Error getting favorite games: $exception", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(requireContext(), "Please log in to see your favorite games", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchBoardGames(favoriteGameIds: List<Int>) {
        db.collection("BoardGames")
            .whereIn("id", favoriteGameIds)
            .get()
            .addOnSuccessListener { result ->
                val games = result.map { document -> document.toObject(BoardGame::class.java) }
                setupRecyclerView(games)
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Error getting games: $exception", Toast.LENGTH_SHORT).show()
            }
    }

    private fun setupRecyclerView(games: List<BoardGame>) {
        val adapter = GameAdapter(games)
        listView.adapter = adapter
        adapter.setOnClickListener(object : GameAdapter.OnClickListener {
            override fun onClick(position: Int, model: BoardGame) {
                val intent = Intent(activity, GameDetailActivity::class.java).apply {
                    putExtra("GAME_ID", model.id)
                }
                startActivity(intent)
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            FavoritesFragment().apply {}
    }
}

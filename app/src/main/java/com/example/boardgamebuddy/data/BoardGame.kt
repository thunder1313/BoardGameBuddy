package com.example.boardgamebuddy.data

data class BoardGame(
    val id: String = "",
    val name: String = "",
    val designer: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val maxPlayers: Int = 0
)

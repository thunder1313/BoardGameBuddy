package com.example.boardgamebuddy.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.boardgamebuddy.R
import com.example.boardgamebuddy.data.BoardGame
import com.example.boardgamebuddy.ui.GameDetailActivity

class GameAdapter(private val games: List<BoardGame>) : RecyclerView.Adapter<GameAdapter.ViewHolder>() {

    private var onClickListener: OnClickListener? = null

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val gameNameTextView: TextView = itemView.findViewById(R.id.gameNameTextView)
        val gameImageView: ImageView = itemView.findViewById(R.id.gameImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_game, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val game = games[position]

        holder.gameNameTextView.text = game.name
        Glide.with(holder.itemView)
            .load(game.imageUrl)
            .into(holder.gameImageView)

        holder.itemView.setOnClickListener {
            onClickListener?.onClick(position, game)
        }
    }

    override fun getItemCount(): Int {
        return games.size
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, model: BoardGame)
    }
}

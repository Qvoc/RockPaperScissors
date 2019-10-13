package com.example.rockpaperscissors.model

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rockpaperscissors.R
import kotlinx.android.synthetic.main.content_main.view.*
import kotlinx.android.synthetic.main.item_game.view.*

class GameAdapter(private val games: List<Game>) : RecyclerView.Adapter<GameAdapter.ViewHolder>() {
    lateinit var context : Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context

        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_game, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return games.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(games[position])
    }

    inner class ViewHolder (itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(game: Game) {
            itemView.tvHistoryMatchResult.text = context.getString(game.result.stringId)
            itemView.tvHistoryDate.text = game.date
            itemView.imgHistoryComputerMove.setImageDrawable(context.getDrawable(game.computerMove.image))
            itemView.imgHistoryPlayerMove.setImageDrawable(context.getDrawable(game.userMove.image))
        }
    }
}
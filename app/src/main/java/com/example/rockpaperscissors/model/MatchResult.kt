package com.example.rockpaperscissors.model

import com.example.rockpaperscissors.R

enum class MatchResult(var stringId: Int) {
    WIN(R.string.player_win),
    LOSE(R.string.computer_win),
    DRAW(R.string.draw)
}
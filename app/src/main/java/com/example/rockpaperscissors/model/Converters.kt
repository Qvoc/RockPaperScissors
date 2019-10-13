
package com.example.rockpaperscissors.model

import androidx.room.TypeConverter
import java.util.*

class Converters {
    @TypeConverter
    fun intToMatchResult(value: Int?): MatchResult? {
        return when(value) {
            -1 -> MatchResult.LOSE
            0 -> MatchResult.DRAW
            1 -> MatchResult.WIN
            else -> null
        }
    }

    @TypeConverter
    fun matchResultToInt(matchResult: MatchResult?): Int? {
        return when(matchResult) {
            MatchResult.LOSE -> -1
            MatchResult.DRAW -> 0
            MatchResult.WIN -> 1
            else -> null
        }
    }

    @TypeConverter
    fun intToMove(value: Int?): Move? {
        return when(value) {
            -1 -> Move.ROCK
            0 -> Move.SCISSORS
            1 -> Move.PAPER
            else -> null
        }
    }

    @TypeConverter
    fun moveToInt(move: Move?): Int? {
        return when(move) {
            Move.ROCK -> -1
            Move.SCISSORS -> 0
            Move.PAPER -> 1
            else -> null
        }
    }
}
package com.example.rockpaperscissors.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.example.rockpaperscissors.R
import com.example.rockpaperscissors.database.GameRepository
import com.example.rockpaperscissors.model.Game
import com.example.rockpaperscissors.model.Move

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.rockpaperscissors.model.MatchResult
import java.text.SimpleDateFormat
import java.util.*

const val GAME_HISTORY_REQUEST_CODE = 100

lateinit var gameRepository: GameRepository
val mainScope = CoroutineScope(Dispatchers.Main)

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        gameRepository = GameRepository(this)
        initViews()

        btnRock.setOnClickListener { displayMatch(Move.ROCK) }
        btnPaper.setOnClickListener { displayMatch(Move.PAPER) }
        btnScissors.setOnClickListener { displayMatch(Move.SCISSORS) }
    }

    private fun initViews() {
        setStatsFromDatabase()
    }

    // Determine and display the stats
    private fun setStatsFromDatabase() {
        mainScope.launch {
            var win = 0
            var draw = 0
            var lose = 0

            val games = withContext(Dispatchers.IO) {
                gameRepository.getAllGames()
            }

            for (match in games) {
                when (match.result) {
                    MatchResult.WIN -> win++
                    MatchResult.DRAW -> draw++
                    MatchResult.LOSE -> lose++
                }
            }

            tvWinHistory.text = getString(R.string.winStat, win.toString())
            tvDrawHistory.text = getString(R.string.drawStat, draw.toString())
            tvLoseHistory.text = getString(R.string.loseStat, lose.toString())
        }
    }

    // Display the match result
    private fun displayMatch(userMove: Move) {
        val computerMove = getComputerMove()
        val matchResult: MatchResult = determineMatchResult(userMove, computerMove)

        mainScope.launch {
            val game = Game(formatDate(), computerMove, userMove, matchResult)
            withContext(Dispatchers.IO) {
                gameRepository.insertGame(game)
            }
            tvMatchResult.text = getString(game.result.stringId)
            imgComputerMove.setImageDrawable(getDrawable(game.computerMove.image))
            imgPlayerMove.setImageDrawable(getDrawable(game.userMove.image))
            setStatsFromDatabase()
        }
    }

    // Determine the result of the match
    private fun determineMatchResult(userMove: Move, computerMove: Move): MatchResult {
        if ((userMove == Move.PAPER && computerMove == Move.SCISSORS)
            || (userMove == Move.ROCK && computerMove == Move.PAPER)
            || (userMove == Move.SCISSORS && computerMove == Move.ROCK)
        ) {
            return MatchResult.LOSE
        } else if ((computerMove == Move.PAPER && userMove == Move.SCISSORS)
            || (computerMove == Move.ROCK && userMove == Move.PAPER)
            || (computerMove == Move.SCISSORS && userMove == Move.ROCK)
        ) {
            return MatchResult.WIN
        } else {
            return MatchResult.DRAW
        }
    }

    private fun formatDate(): String {
        val dateFormatter = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        return dateFormatter.format(Date())
    }

    // Return a random move
    private fun getComputerMove(): Move {
        return when ((1..3).shuffled().first()) {
            1 -> Move.ROCK
            2 -> Move.PAPER
            3 -> Move.SCISSORS
            else -> Move.ROCK
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_match_history -> {
                startMatchHistoryActivity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Start a new activity
    private fun startMatchHistoryActivity() {
        val intent = Intent(this, MatchHistoryActivity::class.java)
        startActivityForResult(intent, GAME_HISTORY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                GAME_HISTORY_REQUEST_CODE -> {
                    setStatsFromDatabase()
                }
            }
        }
    }
}

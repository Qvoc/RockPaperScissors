package com.example.rockpaperscissors.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rockpaperscissors.R
import com.example.rockpaperscissors.model.Game
import com.example.rockpaperscissors.model.GameAdapter

import kotlinx.android.synthetic.main.activity_match_history.*
import kotlinx.android.synthetic.main.content_match_history.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MatchHistoryActivity : AppCompatActivity() {

    private val games = arrayListOf<Game>()
    private val gameAdapter = GameAdapter(games)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match_history)
        setSupportActionBar(toolbar)

        supportActionBar?.title = "Your Game History"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initViews()
    }

    private fun initViews() {
        rvMatchHistory.layoutManager =
            LinearLayoutManager(this@MatchHistoryActivity, RecyclerView.VERTICAL, false)
        rvMatchHistory.adapter = gameAdapter
        rvMatchHistory.addItemDecoration(
            DividerItemDecoration(
                this@MatchHistoryActivity,
                DividerItemDecoration.VERTICAL
            )
        )
        getMatchesFromDatabase()
    }

    // Add all matches from the database
    private fun getMatchesFromDatabase() {
        mainScope.launch {
            val games = withContext(Dispatchers.IO) {
                gameRepository.getAllGames()
            }

            this@MatchHistoryActivity.games.clear()
            this@MatchHistoryActivity.games.addAll(games)
            gameAdapter.notifyDataSetChanged()
        }
    }

    // Delete all matches
    private fun deleteAllGames() {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                gameRepository.deleteAllGames()
            }
            getMatchesFromDatabase()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                setResult(Activity.RESULT_OK, Intent())
                finish()
                true
            }
            R.id.action_delete_history -> {
                deleteAllGames()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_match_history, menu)
        return true
    }

}


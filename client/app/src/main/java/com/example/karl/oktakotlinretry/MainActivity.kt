package com.example.karl.oktakotlinretry

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import net.openid.appauth.AuthState

class MainActivity : AppCompatActivity() {

    lateinit var adapter:MovieAdapter

    fun readAuthState(): AuthState {
        val authPrefs = getSharedPreferences("OktaAppAuthState", Context.MODE_PRIVATE)
        val stateJson = authPrefs.getString("state", "")
        return if (!stateJson!!.isEmpty()) {
            try {
                AuthState.jsonDeserialize(stateJson)
            } catch (exp: org.json.JSONException) {
                AuthState()
            }

        } else {
            AuthState()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = MovieAdapter(this.baseContext, readAuthState().accessToken)

        rv_item_list.layoutManager = LinearLayoutManager(this)
        rv_item_list.adapter = adapter

        fab.setOnClickListener{ showNewDialog() }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.buttons, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.refresh -> {
            adapter.refreshMovies()
            Toast.makeText(this.baseContext, "Refreshed", Toast.LENGTH_LONG).show()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    fun showNewDialog() {
        val dialogBuilder = AlertDialog.Builder(this)

        val input = EditText(this@MainActivity)
        val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT)
        input.layoutParams = lp

        dialogBuilder.setView(input)

        dialogBuilder.setTitle("New Movie")
        dialogBuilder.setMessage("Enter Name Below")
        dialogBuilder.setPositiveButton("Save", { dialog, whichButton ->
            adapter.addMovie(Movie(0,input.text.toString()))
        })
        dialogBuilder.setNegativeButton("Cancel", { dialog, whichButton ->
            //pass
        })
        val b = dialogBuilder.create()
        b.show()
    }

}

package com.example.karl.oktakotlinretry

import android.content.Context
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.list_item.view.*

class MovieAdapter(val context: Context, val token: String?) :
        RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    val client by lazy { MovieApiClient.create() }
    var movies: ArrayList<Movie> = ArrayList()

    init { refreshMovies() }

    class MovieViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MovieAdapter.MovieViewHolder {

        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item, parent, false)

        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.view.name.text = movies[position].name
        holder.view.btnDelete.setOnClickListener { showDeleteDialog(holder, movies[position]) }
        holder.view.btnEdit.setOnClickListener { showUpdateDialog(holder, movies[position]) }
    }

    override fun getItemCount() = movies.size

    fun refreshMovies() {
        client.getMovies("Bearer $token")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            movies.clear()
                            movies.addAll(result.list.movies)
                            notifyDataSetChanged()
                        },
                        { error ->
                            Toast.makeText(context, "Refresh error: ${error.message}", Toast.LENGTH_LONG).show()
                            Log.e("ERRORS", error.message)
                        }
                )
    }

    fun updateMovie(movie: Movie) {
        client.updateMovie(movie.id, movie)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ refreshMovies() }, { throwable ->
                    Toast.makeText(context, "Update error: ${throwable.message}", Toast.LENGTH_LONG).show()
                }
                )
    }

    fun addMovie(movie: Movie) {
        client.addMovie(movie)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ refreshMovies() }, { throwable ->
                    Toast.makeText(context, "Add error: ${throwable.message}", Toast.LENGTH_LONG).show()
                }
                )
    }

    fun deleteMovie(movie: Movie) {

        client.deleteMovie(movie.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ refreshMovies() }, { throwable ->
                    Toast.makeText(context, "Delete error: ${throwable.message}", Toast.LENGTH_LONG).show()
                }
                )

    }

    fun showUpdateDialog(holder: MovieViewHolder, movie: Movie) {
        val dialogBuilder = AlertDialog.Builder(holder.view.context)

        val input = EditText(holder.view.context)
        val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT)
        input.layoutParams = lp
        input.setText(movie.name)

        dialogBuilder.setView(input)

        dialogBuilder.setTitle("Update Movie")
        dialogBuilder.setPositiveButton("Update", { dialog, whichButton ->
            updateMovie(Movie(movie.id,input.text.toString()))
        })
        dialogBuilder.setNegativeButton("Cancel", { dialog, whichButton ->
            dialog.cancel()
        })
        val b = dialogBuilder.create()
        b.show()
    }

    fun showDeleteDialog(holder: MovieViewHolder, movie: Movie) {
        val dialogBuilder = AlertDialog.Builder(holder.view.context)
        dialogBuilder.setTitle("Delete")
        dialogBuilder.setMessage("Confirm delete?")
        dialogBuilder.setPositiveButton("Delete", { dialog, whichButton ->
            deleteMovie(movie)
        })
        dialogBuilder.setNegativeButton("Cancel", { dialog, whichButton ->
            dialog.cancel()
        })
        val b = dialogBuilder.create()
        b.show()
    }


}

package com.cjayme.movielistapplication.presentations.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cjayme.movielistapplication.R
import com.cjayme.movielistapplication.data.Result
import com.cjayme.movielistapplication.presentations.adapter.MovieListAdapter.OnItemClickListener
import com.cjayme.movielistapplication.presentations.home.HomeFragmentDirections


class GenreListAdapter (
    private val results: List<Result>?,
    private val genres: List<String>?,
    private val context: Context,
    private val listener: OnClickListener
) :
    RecyclerView.Adapter<GenreListAdapter.ViewHolder>() {

    interface OnClickListener {
        fun onGenreClick(position: String, itemView: View)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val genre: TextView
        val recycler: RecyclerView

        init {
            // Define click listener for the ViewHolder's View
            genre = view.findViewById(R.id.tv_genre)
            recycler = view.findViewById(R.id.rv_movies)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.listview_genre_group, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        val genre = genres!![position]

        viewHolder.genre.text = genre

        viewHolder.recycler.layoutManager = LinearLayoutManager(
            viewHolder.itemView.context,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        val results = getMoviesByGenre(genres, position)

        viewHolder.recycler.adapter = MovieListAdapter(results, context,
            object : OnItemClickListener {
                override fun onItemClick(trackId: Int, itemView: View) {
                    val action = HomeFragmentDirections.actionNavigationHomeToNavigationDetail(trackId)
                    Navigation.findNavController(viewHolder.itemView).navigate(action)
                }
            })

        viewHolder.genre.setOnClickListener { listener.onGenreClick(genre, viewHolder.itemView) }
    }

    private fun getMoviesByGenre(
        genres: List<String>,
        position: Int
    ) = results!!.filter { it.primaryGenreName.contains(genres[position], ignoreCase = true) }

    override fun getItemCount() = genres!!.size

}

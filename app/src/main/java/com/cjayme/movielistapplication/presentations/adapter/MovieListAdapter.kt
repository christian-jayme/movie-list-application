package com.cjayme.movielistapplication.presentations.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cjayme.movielistapplication.R
import com.cjayme.movielistapplication.data.model.Result
import com.cjayme.movielistapplication.presentations.genre.GenreFragmentDirections
import com.cjayme.movielistapplication.presentations.home.HomeFragmentDirections
import com.cjayme.movielistapplication.utils.Utils


class MovieListAdapter(
    private val results: List<Result>?,
    private val context: Context,
    private val fragment: String
) :
    RecyclerView.Adapter<MovieListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView
        val genre: TextView
        val price: TextView
        val image: ImageView
        val card: CardView
        val star: ImageView

        init {
            // Define click listener for the ViewHolder's View
            title = view.findViewById(R.id.tv_item_title)
            image = view.findViewById(R.id.iv_item_image)
            star = view.findViewById(R.id.iv_star)
            genre = view.findViewById(R.id.tv_genre)
            price = view.findViewById(R.id.tv_price)
            card = view.findViewById(R.id.cv_movie_item)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.listview_grid_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val result = results!![position]

        viewHolder.title.text = result.trackName
        viewHolder.genre.text = result.primaryGenreName
        viewHolder.price.text = buildString {
            append(result.trackPrice)
            append(" • ")
            append(result.currency)
        }

        viewHolder.card.setOnClickListener {
            val action = when (fragment) {
                Utils.GENRE_FRAGMENT -> GenreFragmentDirections.actionNavigationGenreToNavigationDetail(result.trackId)
                else -> HomeFragmentDirections.actionNavigationHomeToNavigationDetail(result.trackId)
            }
            Navigation.findNavController(viewHolder.itemView).navigate(action)
        }

        viewHolder.star.setImageResource(initialStarIcon(result))

        viewHolder.star.setOnClickListener {
            updateStarIcon(result, viewHolder)
        }

        Glide.with(context)
            .load(result.artworkUrl100)
            .placeholder(R.drawable.woman)
            .into(viewHolder.image)
    }
    private fun initialStarIcon(result: Result): Int {
        return when(Utils.isDataExistsInFavoriteList(
            context,
            "track_id",
            result.trackId.toString()
        )) {
            true -> {
                R.drawable.round_gold_star_rate_24
            }
            false -> {
                R.drawable.round_gray_star_rate_24
            }
        }

    }
    private fun updateStarIcon(result: Result, viewHolder: ViewHolder) {
        when(Utils.isDataExistsInFavoriteList(
            context,
            "track_id",
            result.trackId.toString()
        )) {
            true -> {
                Utils.removeFavoriteFromList(
                    context,
                    "track_id",
                    result.trackId.toString()
                )
                viewHolder.star.setImageResource(R.drawable.round_gray_star_rate_24)
            }
            false -> {
                Utils.saveFavoriteToList(
                    context,
                    "track_id",
                    result.trackId.toString()
                )
                viewHolder.star.setImageResource(R.drawable.round_gold_star_rate_24)
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = results!!.size

}

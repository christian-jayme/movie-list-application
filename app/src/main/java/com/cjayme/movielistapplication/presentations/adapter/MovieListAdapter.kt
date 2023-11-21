package com.cjayme.movielistapplication.presentations.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cjayme.movielistapplication.R
import com.cjayme.movielistapplication.data.Result


class MovieListAdapter(
    private val results: List<Result>?,
    private val context: Context,
    private val listener: OnItemClickListener
) :
    RecyclerView.Adapter<MovieListAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int, itemView: View)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView
        val genre: TextView
        val price: TextView
        val image: ImageView
        val card: CardView

        init {
            // Define click listener for the ViewHolder's View
            title = view.findViewById(R.id.tv_item_title)
            image = view.findViewById(R.id.iv_item_image)
            genre = view.findViewById(R.id.tv_genre)
            price = view.findViewById(R.id.tv_price)
            card = view.findViewById(R.id.cv_movie_item)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.listview_movie, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.title.text = results!![position].trackName
        viewHolder.genre.text = results[position].primaryGenreName
        viewHolder.price.text = buildString {
            append(results[position].trackPrice)
            append(" • ")
            append(results[position].currency)
        }
        viewHolder.card.setOnClickListener {
            listener.onItemClick(results[position].trackId, viewHolder.itemView)
        }

        Glide.with(context)
            .load(results[position].artworkUrl100)
            .placeholder(R.drawable.woman)
            .into(viewHolder.image)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = results!!.size

}

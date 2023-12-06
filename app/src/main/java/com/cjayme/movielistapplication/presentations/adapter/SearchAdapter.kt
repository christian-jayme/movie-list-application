package com.cjayme.movielistapplication.presentations.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cjayme.movielistapplication.R
import com.cjayme.movielistapplication.data.model.Result

class SearchAdapter(
    private val context: Context,
    private var results: List<Result>?,
    private val listener: OnClickListener
) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    interface OnClickListener {
        fun onItemClick(position: Int, itemView: View)
    }

    private var filteredData: List<Result>? = listOf()

    @SuppressLint("NotifyDataSetChanged")
    fun filter(query: String) {

        val tempResult = results?.filter {
            it.trackName.contains(query, ignoreCase = true)
        }

        filteredData = if(tempResult.isNullOrEmpty() || query.isEmpty()) {
            listOf()
        } else {
            tempResult
        }

        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // view holder implementation
        val title: TextView
        val genre: TextView
        val price: TextView
        val description: TextView
        val image: ImageView
        val item: CardView

        init {
            // Define click listener for the ViewHolder's View
            title = view.findViewById(R.id.tv_item_title)
            image = view.findViewById(R.id.iv_item_image)
            genre = view.findViewById(R.id.tv_genre)
            price = view.findViewById(R.id.tv_price)
            description = view.findViewById(R.id.tv_short_description)
            item = view.findViewById(R.id.cv_list_item)
        }

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // create view holder
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.listview_list_item, viewGroup, false)
        Log.d("CHUCHU", "filter: ${filteredData!!.size}")
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // bind data to view holder
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        Log.d("CHUCHU", "onBindViewHolder")
        val item = filteredData!![position]

        viewHolder.title.text = item.trackName
        viewHolder.genre.text = item.primaryGenreName
        viewHolder.description.text = item.shortDescription


        viewHolder.price.text = buildString {
            append(item.trackPrice)
            append(" • ")
            append(item.currency)
        }
        viewHolder.item.setOnClickListener {
            listener.onItemClick(item.trackId, viewHolder.itemView)
        }

        Glide.with(context)
            .load(item.artworkUrl100)
            .placeholder(R.drawable.woman)
            .into(viewHolder.image)
    }

    override fun getItemCount(): Int {
        return filteredData!!.size
    }
}

package com.cjayme.movielistapplication.presentations.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.cjayme.movielistapplication.R


class CarouselAdapter(
    private val images: List<Int>
) : RecyclerView.Adapter<CarouselAdapter.ViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var imageView: ImageView
        init {
            // Define click listener for the ViewHolder's View
            imageView = view.findViewById(R.id.carousel_image_view)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.listview_carousel, parent, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.imageView.setOnClickListener {
            onItemClickListener?.onClick(viewHolder.imageView, images[position])
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = images.size



    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }


    interface OnItemClickListener {
        fun onClick(imageView: ImageView, path: Int)
    }
}

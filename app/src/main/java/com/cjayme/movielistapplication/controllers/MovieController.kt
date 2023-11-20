package com.cjayme.movielistapplication.controllers

import android.content.Context
import com.cjayme.movielistapplication.R
import com.cjayme.movielistapplication.listeners.OnMovieResultResponse
import com.cjayme.movielistapplication.network.ApiService
import com.cjayme.movielistapplication.network.RetrofitClient
import com.cjayme.movielistapplication.utils.Utils
import javax.inject.Inject

class MovieController @Inject constructor(
    private val apiService: ApiService, private val context: Context
) {

    // Listener instance
    private var listener: OnMovieResultResponse? = null

    // Function to set the listener
    fun setMovieResultListener(listener: OnMovieResultResponse) {
        this.listener = listener
    }

    suspend fun getMovies(term: String, country: String, media: String) {
        if (!Utils.isNetworkAvailable(context)) {
            listener?.onError(R.string.no_internet)
        }

         try {
            val response = apiService.getMovieList(term, country, media)

            if (response.resultCount > 0) {
                // Return the list of movies if there are results
                listener?.onSuccess(response.results )

            } else {
                // Return null or handle no results scenario
                listener?.onError(R.string.unknown_err)
            }
        } catch (e: Exception) {
            // Handle exceptions or errors (log/print for simplicity)
            e.printStackTrace()
            // Return null in case of any exception
            listener?.onError(R.string.unknown_err)
        }
    }
}
package com.cjayme.movielistapplication.network

import com.cjayme.movielistapplication.data.model.Movie
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("search")
    suspend fun getMovieList(
        @Query("term") term: String,
        @Query("country") country: String,
        @Query("media") media: String
    ): Movie
}
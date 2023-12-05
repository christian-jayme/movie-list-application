package com.cjayme.movielistapplication.controllers

import androidx.room.withTransaction
import com.cjayme.movielistapplication.data.database.AppDatabase
import com.cjayme.movielistapplication.network.ApiService
import com.cjayme.movielistapplication.utils.Utils
import kotlinx.coroutines.delay
import javax.inject.Inject

class MovieController @Inject constructor(
    private val apiService: ApiService,
    private val db : AppDatabase
) {
    private val movieDao = db.movieDao()

    fun getMovies(
        term: String,
        country: String,
        media: String) = Utils.networkBoundResource(
            query = {
                movieDao.getAllMovies()
            },
            fetch = {
                delay(2000)
                apiService.getMovieList(term, country, media)
            },
            saveFetchResult = {movies ->
                db.withTransaction {
                    movieDao.deleteAllResults()
                    movieDao.insertMovies(movies)
                }
            }
        )
}
package com.cjayme.movielistapplication.presentations.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.cjayme.movielistapplication.controllers.MovieController
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor (
    controller: MovieController
): ViewModel() {

    val movies = controller
        .getMovies("star", "au", "movie")
        .asLiveData()
}
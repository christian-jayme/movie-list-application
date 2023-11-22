package com.cjayme.movielistapplication.presentations.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.cjayme.movielistapplication.controllers.MovieController
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor (
    controller: MovieController
): ViewModel() {

    val movies = controller
        .getMovies("star", "au", "movie")
        .asLiveData()


    private val _text = MutableLiveData<String>().apply {
        value = "This is search Fragment"
    }
    val text: LiveData<String> = _text
}
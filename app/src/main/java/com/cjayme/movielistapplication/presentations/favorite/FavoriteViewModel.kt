package com.cjayme.movielistapplication.presentations.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.cjayme.movielistapplication.BaseApplication
import com.cjayme.movielistapplication.controllers.MovieController
import com.cjayme.movielistapplication.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor (
    controller: MovieController,
    application: BaseApplication
) : ViewModel() {

    val movies = controller
        .getMovies("star", "au", "movie")
        .asLiveData()

    private val _text = MutableLiveData<String>().apply {
        value = "This is favorite Fragment"
    }
    val text: LiveData<String> = _text
}
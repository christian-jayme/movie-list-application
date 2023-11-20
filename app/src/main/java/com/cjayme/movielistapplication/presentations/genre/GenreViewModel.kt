package com.cjayme.movielistapplication.presentations.genre

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GenreViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Genre Fragment"
    }
    val text: LiveData<String> = _text
}
package com.cjayme.movielistapplication.listeners

import com.cjayme.movielistapplication.models.Result

interface OnMovieResultResponse {
    fun onSuccess(movies: List<Result>?)
    fun onError(errorMessage: Int)
}
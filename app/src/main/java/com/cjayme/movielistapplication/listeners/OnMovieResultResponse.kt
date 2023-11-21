package com.cjayme.movielistapplication.listeners

import com.cjayme.movielistapplication.data.Result

interface OnMovieResultResponse {
    fun onSuccess(movies: List<Result>?)
    fun onError(errorMessage: Int)
}
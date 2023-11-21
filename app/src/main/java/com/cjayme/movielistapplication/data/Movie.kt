package com.cjayme.movielistapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey val resultCount: Int,
    val results: List<Result>
)
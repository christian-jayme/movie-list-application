package com.cjayme.movielistapplication.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cjayme.movielistapplication.data.dao.MovieDao
import com.cjayme.movielistapplication.data.model.Movie
import com.cjayme.movielistapplication.utils.Converters

@Database(entities = [Movie::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao

}
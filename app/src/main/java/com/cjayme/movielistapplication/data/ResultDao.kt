package com.cjayme.movielistapplication.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ResultDao {

    @Query("SELECT * FROM results")
    suspend fun getAllResult (): Flow<List<Result>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResult(vararg results: List<Result>)

    @Query("DELETE FROM results")
    suspend fun deleteAllResults()
}
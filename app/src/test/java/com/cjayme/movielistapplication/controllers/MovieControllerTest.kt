package com.cjayme.movielistapplication.controllers

import android.content.Context
import android.os.Build.VERSION_CODES.S
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cjayme.movielistapplication.data.database.AppDatabase
import com.cjayme.movielistapplication.network.ApiService
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(AndroidJUnit4::class)
@Config(sdk = [S])
class MovieControllerTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    private val context: Context = ApplicationProvider.getApplicationContext()
    private lateinit var repository: MovieController

    @Before
    fun init() {
        val database: AppDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()

        val baseUrl = "https://itunes.apple.com/"

        val okHttpClient = OkHttpClient
            .Builder()
            .build()
        val service = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .build().create(ApiService::class.java)

        repository = MovieController(service, database)
    }

    @Test
    fun testApiSuccess() {
        val response = runBlocking { repository.apiService.getMovieList("star", "au", "movie") }
        println(response)
        Assert.assertNotNull(response)
    }
}
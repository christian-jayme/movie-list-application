package com.cjayme.movielistapplication.utils

import android.R.attr
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AlertDialog
import com.cjayme.movielistapplication.R
import com.cjayme.movielistapplication.data.model.Result
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale


private const val SHARED_PREF = "SaveDataPreference"
private const val RECENTLY_VIEWED_KEY = "recently_viewed"
private const val FAVORITE_KEY = "track_id"

object Utils {
    const val GENRE_FRAGMENT = "GenreFragment"
    const val HOME_FRAGMENT = "HomeFragment"
    const val SEARCH_FRAGMENT = "SearchFragment"
    const val FAVORITE_FRAGMENT = "FavoriteFragment"
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

        connectivityManager?.let {
            val network = it.activeNetwork ?: return false
            val capabilities = it.getNetworkCapabilities(network) ?: return false
            return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        }
        return false
    }

    fun showErrorDialog(context: Context, errorMessage: Int) {
        AlertDialog.Builder(context)
            .setMessage(errorMessage)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    fun <ResultType, RequestType> networkBoundResource(
        query: () -> Flow<ResultType>,
        fetch: suspend () -> RequestType,
        saveFetchResult: suspend (RequestType) -> Unit,
        shouldFetch: (ResultType) -> Boolean = { true }
    ) = flow {
        val data = query().first()

        val flow = if(shouldFetch(data)) {
            emit(Resource.Loading(data))
            try {
                saveFetchResult(fetch())
                query().map { Resource.Success(it) }
            } catch (e: Throwable) {
                query().map { Resource.Error(e,it) }
            }
        } else {
            query().map { Resource.Success(it) }
        }
        emitAll(flow)
    }

    fun convertDate (date: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

        try {
            val parsedDate: Date = inputFormat.parse(date) ?: Date()
            return outputFormat.format(parsedDate)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
    }

    fun saveViewDateToList(context: Context, trackId: String) {
        val formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy")
        val currentDate = LocalDateTime.now().format(formatter)

        val sharedPref = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        val list = sharedPref.getStringSet("recently_viewed", null) ?: mutableSetOf()
        val tempList = list.toMutableSet()

        tempList.add("$currentDate:$trackId")

        with (sharedPref.edit()) {
            putStringSet("recently_viewed", tempList)
            apply()
        }
    }

    fun getViewDateList(context: Context, trackId: String): String? {
        val sharedPref = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        val list = sharedPref.getStringSet(RECENTLY_VIEWED_KEY, null)?.toList()
        return list?.firstOrNull { it.contains(trackId)}
    }

    fun saveFavoriteToList(context: Context, value: String) {
        val sharedPref = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        val list = sharedPref.getStringSet(FAVORITE_KEY, null) ?: mutableSetOf()
        val tempList = list.toMutableSet()

        tempList.add(value)

        with (sharedPref.edit()) {
            putStringSet(FAVORITE_KEY, tempList)
            apply()
        }
    }

    fun removeFavoriteFromList(context: Context, value: String) {
        val sharedPref = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        val list = sharedPref.getStringSet(FAVORITE_KEY, null) ?: return
        val tempList = list.toMutableSet()

        tempList.remove(value)

        with (sharedPref.edit()) {
            putStringSet(FAVORITE_KEY, tempList)
            apply()
        }
    }

    fun getFavoriteList(context: Context): List<String>? {
        val sharedPref = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        return sharedPref.getStringSet(FAVORITE_KEY, null)?.toList()
    }

    fun isDataExistsInFavoriteList(context: Context, data: String): Boolean {
        val sharedPref = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        val list = sharedPref.getStringSet(FAVORITE_KEY, null) ?: return false
        return list.contains(data)
    }

    fun getFavoriteMovie(context: Context, res: List<Result>?): List<Result> {
        val favoriteMovies = getFavoriteList(context)

        return favoriteMovies?.let {
            res?.filter { movie ->
                it.contains(movie.trackId.toString())
            }
        } ?: listOf()
    }
}
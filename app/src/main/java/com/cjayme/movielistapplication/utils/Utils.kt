package com.cjayme.movielistapplication.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AlertDialog
import com.cjayme.movielistapplication.R
import com.cjayme.movielistapplication.data.model.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val SHARED_PREF = "SaveDataPreference"

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

    fun saveFavoriteToList(context: Context, key: String, value: String) {
        val sharedPref = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        val list = sharedPref.getStringSet(key, null) ?: mutableSetOf()
        val listCopy = list.toMutableSet()

        listCopy.add(value)

        with (sharedPref.edit()) {
            putStringSet(key, listCopy)
            apply()
        }
    }

    fun removeFavoriteFromList(context: Context, key: String, value: String) {
        val sharedPref = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        val list = sharedPref.getStringSet(key, null) ?: return
        val listCopy = list.toMutableSet()

        listCopy.remove(value)

        with (sharedPref.edit()) {
            putStringSet(key, listCopy)
            apply()
        }
    }

    fun getFavoriteList(context: Context, key: String): List<String>? {
        val sharedPref = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        return sharedPref.getStringSet(key, null)?.toList()
    }

    fun isDataExistsInFavoriteList(context: Context, key: String, data: String): Boolean {
        val sharedPref = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        val list = sharedPref.getStringSet(key, null) ?: return false
        return list.contains(data)
    }

    fun getFavoriteMovie(context: Context, res: List<Result>?): List<Result>? {
        val favoriteMovies = getFavoriteList(context, "track_id")

        return res?.filter { movie ->
            favoriteMovies!!.contains(movie.trackId.toString())
        }
    }
}
package com.cjayme.movielistapplication.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AlertDialog
import com.cjayme.movielistapplication.R
import kotlinx.coroutines.flow.*

object Utils {

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
}
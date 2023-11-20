package com.cjayme.movielistapplication.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AlertDialog
import com.cjayme.movielistapplication.R

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
}
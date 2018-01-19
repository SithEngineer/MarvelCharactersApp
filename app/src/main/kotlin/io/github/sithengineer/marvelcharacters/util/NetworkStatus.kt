package io.github.sithengineer.marvelcharacters.util

import android.net.ConnectivityManager

class NetworkStatus(private val connectivityManager: ConnectivityManager) {
  fun available(): Boolean {
    return connectivityManager.activeNetworkInfo.isConnectedOrConnecting
  }
}
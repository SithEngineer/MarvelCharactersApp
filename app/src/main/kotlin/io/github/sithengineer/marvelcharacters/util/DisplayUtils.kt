package io.github.sithengineer.marvelcharacters.util

import android.content.Context

object DisplayUtils {
  fun convertDpToPixel(context: Context?, dp: Int): Int {
    if (context != null) {
      val density = context.resources.displayMetrics.density
      return Math.round(dp.toFloat() * density)
    }
    return 0
  }
}
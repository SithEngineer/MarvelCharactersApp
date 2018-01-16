package io.github.sithengineer.marvelcharacters

import android.app.Application
import com.facebook.stetho.Stetho
import com.facebook.stetho.timber.StethoTree
import io.github.sithengineer.marvelcharacters.log.CrashReportTree
import timber.log.Timber


class MarvelCharactersApplication : Application() {
  override fun onCreate() {
    super.onCreate()
    if (BuildConfig.DEBUG) {
      Stetho.initializeWithDefaults(applicationContext)
      Timber.plant(StethoTree())
    } else {
      Timber.plant(CrashReportTree())
    }
  }
}
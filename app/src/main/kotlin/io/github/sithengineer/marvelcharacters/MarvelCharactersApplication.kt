package io.github.sithengineer.marvelcharacters

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.os.StrictMode
import com.facebook.cache.disk.DiskCacheConfig
import com.facebook.common.logging.FLog
import com.facebook.common.util.ByteConstants
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.core.ImagePipelineConfig
import com.facebook.imagepipeline.listener.RequestListener
import com.facebook.imagepipeline.listener.RequestLoggingListener
import com.facebook.stetho.Stetho
import com.facebook.stetho.timber.StethoTree
import io.github.sithengineer.marvelcharacters.data.source.CharactersRepository
import io.github.sithengineer.marvelcharacters.data.source.remote.CharactersRemoteDataSource
import io.github.sithengineer.marvelcharacters.data.source.remote.MarvelServiceFactory
import io.github.sithengineer.marvelcharacters.util.NetworkStatus
import io.github.sithengineer.marvelcharacters.log.CrashReportTree
import timber.log.Timber

class MarvelCharactersApplication : Application() {

  override fun onCreate() {
    super.onCreate()
    if (BuildConfig.DEBUG) {
      initializeStetho(applicationContext)
      enableStrictMode()
    } else {
      Timber.plant(CrashReportTree())
    }

    setupFresco(BuildConfig.DEBUG)

    createCharactersRepository(this)
  }

  private fun setupFresco(debug: Boolean) {
    val diskCacheConfig = DiskCacheConfig.newBuilder(this).setBaseDirectoryPath(cacheDir)
        .setBaseDirectoryName("characters_v1")
        .setMaxCacheSize((100 * ByteConstants.MB).toLong())
        .setMaxCacheSizeOnLowDiskSpace((20 * ByteConstants.MB).toLong())
        .setMaxCacheSizeOnVeryLowDiskSpace((5 * ByteConstants.MB).toLong())
        .setVersion(1)
        .build()

    val requestListeners = mutableSetOf<RequestListener>()
    if (debug) {
      requestListeners.add(RequestLoggingListener())
    }

    val imagePipelineConfig = ImagePipelineConfig.newBuilder(this)
        .setRequestListeners(requestListeners)
        .setMainDiskCacheConfig(diskCacheConfig)
        .build()

    Fresco.initialize(this, imagePipelineConfig)

    if (debug) {
      FLog.setMinimumLoggingLevel(FLog.VERBOSE)
    }
  }

  private fun enableStrictMode() {
    StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
        .detectDiskReads()
        .detectDiskWrites()
        .detectNetwork()
        .penaltyLog()
        .build())

    StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
        .detectLeakedSqlLiteObjects()
        .detectLeakedClosableObjects()
        .penaltyLog()
        .penaltyDeath()
        .build())
  }

  private fun initializeStetho(context: Context) {
    val initializer = Stetho.newInitializerBuilder(context)
        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(context))
        .enableDumpapp(Stetho.defaultDumperPluginsProvider(context))
        .build()

    Stetho.initialize(initializer)

    Timber.plant(StethoTree())
  }

  private fun createCharactersRepository(context: Context) {
    val connectivityManager = context.getSystemService(
        Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val marvelApi = MarvelServiceFactory.makeMarvelCharactersApi(BuildConfig.DEBUG,
        context.cacheDir, BuildConfig.CACHE_SIZE_IN_BYTES, NetworkStatus(connectivityManager))

    val charactersRemoteDataSource = CharactersRemoteDataSource(marvelApi,
        BuildConfig.MARVEL_API_PUBLIC_KEY, BuildConfig.MARVEL_API_PRIVATE_KEY)

    charactersRepository = CharactersRepository(charactersRemoteDataSource)
  }

  lateinit var charactersRepository: CharactersRepository
}
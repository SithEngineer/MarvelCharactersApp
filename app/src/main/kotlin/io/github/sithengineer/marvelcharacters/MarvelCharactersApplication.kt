package io.github.sithengineer.marvelcharacters

import android.app.Application
import android.content.Context
import com.facebook.stetho.Stetho
import com.facebook.stetho.timber.StethoTree
import io.github.sithengineer.marvelcharacters.data.source.CharactersRepository
import io.github.sithengineer.marvelcharacters.data.source.remote.CharactersRemoteDataSource
import io.github.sithengineer.marvelcharacters.data.source.remote.MarvelServiceFactory
import io.github.sithengineer.marvelcharacters.util.NetworkStatus
import io.github.sithengineer.marvelcharacters.util.log.CrashReportTree
import timber.log.Timber


class MarvelCharactersApplication : Application() {
  override fun onCreate() {
    super.onCreate()
    if (BuildConfig.DEBUG) {
      initializeStetho(applicationContext)
    } else {
      Timber.plant(CrashReportTree())
    }

    createCharactersRepository(this)
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
    val marvelApi = MarvelServiceFactory.makeMarvelCharactersApi(BuildConfig.DEBUG,
        context.cacheDir, BuildConfig.CACHE_SIZE, NetworkStatus())
    val charactersRemoteDataSource = CharactersRemoteDataSource(marvelApi,
        BuildConfig.MARVEL_API_PUBLIC_KEY, BuildConfig.MARVEL_API_PRIVATE_KEY)
    charactersRepository = CharactersRepository(charactersRemoteDataSource)
  }

  lateinit var charactersRepository: CharactersRepository
}
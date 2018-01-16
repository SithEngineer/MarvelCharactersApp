package io.github.sithengineer.marvelcharacters.data.source.remote

import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import io.github.sithengineer.marvelcharacters.BuildConfig
import io.github.sithengineer.marvelcharacters.util.NetworkStatus
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File

object MarvelServiceFactory {

  fun makeMarvelCharactersApi(isDebug: Boolean = BuildConfig.DEBUG, cacheDirectory: File,
      cacheSize: Long, networkStatus: NetworkStatus): MarvelCharactersApi {
    val okHttpClient = makeOkHttp(makeLoggingInterceptor(isDebug),
        makeForceCacheInterceptor(networkStatus), cacheDirectory, cacheSize)
    return makeRetrofit(okHttpClient, makeMoshi()).create(MarvelCharactersApi::class.java)
  }

  private fun makeForceCacheInterceptor(networkStatus: NetworkStatus): Interceptor {
    return object : Interceptor {
      override fun intercept(chain: Interceptor.Chain?): Response {
        val builder = chain!!.request().newBuilder()
        if (!networkStatus.available()) {
          builder.cacheControl(CacheControl.FORCE_CACHE)
        }
        return chain.proceed(builder.build())
      }
    }
  }

  private fun makeLoggingInterceptor(isDebug: Boolean): HttpLoggingInterceptor {
    val logging = HttpLoggingInterceptor()
    logging.level = if (isDebug)
      HttpLoggingInterceptor.Level.BODY
    else
      HttpLoggingInterceptor.Level.NONE
    return logging
  }

  private fun makeOkHttp(httpLoggingInterceptor: Interceptor, forceCacheInterceptor: Interceptor,
      cacheDirectory: File,
      cacheSize: Long): OkHttpClient {
    return OkHttpClient.Builder()
        .cache(Cache(cacheDirectory, cacheSize))
        .addInterceptor(httpLoggingInterceptor)
        .addInterceptor(forceCacheInterceptor)
        .build()
  }

  private fun makeMoshi(): Moshi {
    return Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
  }

  private fun makeRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit {
    return Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(okHttpClient)
        .baseUrl(BuildConfig.MARVEL_API_URL)
        .build()
  }
}
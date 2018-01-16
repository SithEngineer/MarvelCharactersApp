package io.github.sithengineer.marvelcharacters.data.source.remote

import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import io.github.sithengineer.marvelcharacters.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object MarvelServiceFactory {

  fun makeMarvelCharactersApi(isDebug: Boolean = BuildConfig.DEBUG): MarvelCharactersApi {
    val okHttpClient = makeOkHttp(makeLoggingInterceptor(isDebug))
    return makeRetrofit(okHttpClient, makeMoshi()).create(MarvelCharactersApi::class.java)
  }

  private fun makeLoggingInterceptor(isDebug: Boolean): HttpLoggingInterceptor {
    val logging = HttpLoggingInterceptor()
    logging.level = if (isDebug)
      HttpLoggingInterceptor.Level.BODY
    else
      HttpLoggingInterceptor.Level.NONE
    return logging
  }

  private fun makeOkHttp(httpLoggingInterceptor: Interceptor): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
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
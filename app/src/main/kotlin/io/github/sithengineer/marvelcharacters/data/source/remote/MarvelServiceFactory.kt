package io.github.sithengineer.marvelcharacters.data.source.remote

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.github.sithengineer.marvelcharacters.BuildConfig
import io.github.sithengineer.marvelcharacters.util.NetworkStatus
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File


object MarvelServiceFactory {

  fun makeMarvelCharactersApi(isDebug: Boolean = BuildConfig.DEBUG, cacheDirectory: File,
      cacheSize: Long, networkStatus: NetworkStatus): MarvelCharactersApi {
    val okHttpClient = makeOkHttp(makeLoggingInterceptor(isDebug),
        makeForceCacheInterceptor(networkStatus), cacheDirectory, cacheSize, isDebug)
    return makeRetrofit(okHttpClient, makeGson()).create(MarvelCharactersApi::class.java)
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
      HttpLoggingInterceptor.Level.HEADERS
    else
      HttpLoggingInterceptor.Level.NONE
    return logging
  }

  private fun makeOkHttp(httpLoggingInterceptor: Interceptor, forceCacheInterceptor: Interceptor,
      cacheDirectory: File,
      cacheSize: Long, isDebug: Boolean): OkHttpClient {
    val clientBuilder = OkHttpClient.Builder()
        .cache(Cache(cacheDirectory, cacheSize))
        .addInterceptor(httpLoggingInterceptor)
        .addInterceptor(forceCacheInterceptor)

    if (isDebug) {
      clientBuilder.addNetworkInterceptor(StethoInterceptor())
    }

    return clientBuilder.build()
  }

  private fun makeGson(): Gson {
    return GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
        .create()
  }

  private fun makeRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
    return Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(okHttpClient)
        .baseUrl(BuildConfig.MARVEL_API_URL)
        .build()
  }
}
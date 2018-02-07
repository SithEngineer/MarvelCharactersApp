package io.github.sithengineer.marvelcharacters.data.source.remote

import io.github.sithengineer.marvelcharacters.data.source.apimodel.*
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MarvelCharactersApi {

  @GET("v1/public/characters")
  fun getCharacters(@Query("offset") offset: Int, @Query("limit") limit: Int, @Query("apikey") publicApiKey: String, @Query(
      "ts") timestamp: String, @Query("hash") keyHash: String)
      : Single<DataWrapper<Character>>

  @GET("v1/public/characters")
  fun getCharacters(@Query("nameStartsWith") nameStartsWith: String, @Query("apikey") publicApiKey: String, @Query(
      "ts") timestamp: String, @Query("hash") keyHash: String)
      : Single<DataWrapper<Character>>

  @GET("v1/public/characters/{characterId}")
  fun getCharacter(@Path("characterId") characterId: Int, @Query("apikey") publicApiKey: String, @Query(
      "ts") timestamp: String, @Query("hash") keyHash: String)
      : Single<DataWrapper<Character>>

  @GET("v1/public/characters/{characterId}/comics")
  fun getCharacterComics(@Path("characterId") characterId: Int, @Query("apikey") publicApiKey: String, @Query(
      "ts") timestamp: String, @Query("hash") keyHash: String)
      : Single<DataWrapper<Comic>>

  @GET("v1/public/characters/{characterId}/events")
  fun getCharacterEvents(@Path("characterId") characterId: Int, @Query("apikey") publicApiKey: String, @Query(
      "ts") timestamp: String, @Query("hash") keyHash: String)
      : Single<DataWrapper<Event>>

  @GET("v1/public/characters/{characterId}/series")
  fun getCharacterSeries(@Path("characterId") characterId: Int, @Query("apikey") publicApiKey: String, @Query(
      "ts") timestamp: String, @Query("hash") keyHash: String)
      : Single<DataWrapper<Series>>

  @GET("v1/public/characters/{characterId}/stories")
  fun getCharacterStories(@Path("characterId") characterId: Int, @Query("apikey") publicApiKey: String, @Query(
      "ts") timestamp: String, @Query("hash") keyHash: String)
      : Single<DataWrapper<Story>>

}
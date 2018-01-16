package io.github.sithengineer.marvelcharacters.data.source.remote

import io.github.sithengineer.marvelcharacters.data.model.CharacterDataWrapper
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MarvelCharactersApi {

  @GET("v1/public/characters")
  fun getCharacters(@Query("offset") offset: Int, @Query("apikey") apiKey: String)
      : Single<CharacterDataWrapper>

  @GET("v1/public/characters")
  fun getCharacters(@Query("nameStartsWith") nameStartsWith: String, @Query("apikey") apiKey: String)
      : Single<CharacterDataWrapper>

  @GET("v1/public/characters/{characterId}")
  fun getCharacter(@Path("characterId") characterId: Int, @Query("apikey") apiKey: String)
      : Single<CharacterDataWrapper>

}
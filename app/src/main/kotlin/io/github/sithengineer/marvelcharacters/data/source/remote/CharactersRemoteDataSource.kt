package io.github.sithengineer.marvelcharacters.data.source.remote

import io.github.sithengineer.marvelcharacters.data.model.CharacterDataWrapper
import io.github.sithengineer.marvelcharacters.data.source.CharactersDataSource
import io.reactivex.Single

class CharactersRemoteDataSource(private val api: MarvelCharactersApi,
    private val apiKey: String) : CharactersDataSource {

  override fun getCharacters(offset: Int): Single<CharacterDataWrapper> {
    return api.getCharacters(offset, apiKey)
  }

}
package io.github.sithengineer.marvelcharacters.data.source

import io.github.sithengineer.marvelcharacters.data.model.CharacterDataWrapper
import io.reactivex.Single

interface CharactersDataSource {
  fun getCharacters(offset: Int, limit: Int): Single<CharacterDataWrapper>
}
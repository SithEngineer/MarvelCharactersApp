package io.github.sithengineer.marvelcharacters.data.source

import io.github.sithengineer.marvelcharacters.data.source.apimodel.*
import io.reactivex.Single

interface CharactersDataSource {
  fun getCharacters(offset: Int, limit: Int): Single<DataWrapper<Character>>
  fun getCharacters(nameStartsWith: String): Single<DataWrapper<Character>>
  fun getCharacter(characterId: Int): Single<DataWrapper<Character>>
  fun getCharacterComics(characterId: Int): Single<DataWrapper<Comic>>
  fun getCharacterEvents(characterId: Int): Single<DataWrapper<Event>>
  fun getCharacterSeries(characterId: Int): Single<DataWrapper<Series>>
  fun getCharacterStories(characterId: Int): Single<DataWrapper<Story>>
}
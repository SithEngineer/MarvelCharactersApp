package io.github.sithengineer.marvelcharacters.data.source.local

import io.github.sithengineer.marvelcharacters.data.source.apimodel.Character
import io.github.sithengineer.marvelcharacters.data.source.apimodel.Comic
import io.github.sithengineer.marvelcharacters.data.source.apimodel.DataWrapper
import io.github.sithengineer.marvelcharacters.data.source.apimodel.Event
import io.github.sithengineer.marvelcharacters.data.source.apimodel.Series
import io.github.sithengineer.marvelcharacters.data.source.apimodel.Story
import io.github.sithengineer.marvelcharacters.data.source.CharactersDataSource
import io.reactivex.Single

class CharactersInMemoryDataSource(private val charactersDataSource: CharactersDataSource) :
    CharactersDataSource {



  override fun getCharacters(offset: Int, limit: Int): Single<DataWrapper<Character>> {
    return charactersDataSource.getCharacters(offset, limit)
  }

  override fun getCharacters(nameStartsWith: String): Single<DataWrapper<Character>> {
    TODO("not implemented")
  }

  private val characters: HashMap<Int, Character> = hashMapOf()
  override fun getCharacter(characterId: Int): Single<DataWrapper<Character>> {
    if(characters.containsKey(characterId)){
      TODO("not implemented")
      //return createDataWrapperFor(characters[characterId]!!)
    }

    return charactersDataSource.getCharacter(characterId)
  }

  override fun getCharacterComics(characterId: Int): Single<DataWrapper<Comic>> {
    TODO("not implemented")
  }

  override fun getCharacterEvents(characterId: Int): Single<DataWrapper<Event>> {
    TODO("not implemented")
  }

  override fun getCharacterSeries(characterId: Int): Single<DataWrapper<Series>> {
    TODO("not implemented")
  }

  override fun getCharacterStories(characterId: Int): Single<DataWrapper<Story>> {
    TODO("not implemented")
  }
}
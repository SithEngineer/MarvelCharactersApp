package io.github.sithengineer.marvelcharacters.data.source

import io.github.sithengineer.marvelcharacters.data.source.apimodel.*
import io.reactivex.Single

//class CharactersRepository private constructor(
class CharactersRepository (
    private val remoteDataSource: CharactersDataSource) : CharactersDataSource {

  // singleton with double check lock
  // from https://github.com/googlesamples/android-architecture-components/blob/master/BasicRxJavaSampleKotlin/app/src/main/java/com/example/android/observability/persistence/UsersDatabase.kt
  /*
  // commented out since mockito evaluates that CharactersDataSource (in this object) is never called
  companion object {
    @Volatile private var INSTANCE: CharactersRepository? = null

    fun getInstance(remoteDataSource: CharactersDataSource): CharactersRepository =
        INSTANCE ?: synchronized(this) {
          INSTANCE ?: CharactersRepository(remoteDataSource).also { INSTANCE = it }
        }
  }
  */

  override fun getCharacter(characterId: Int): Single<DataWrapper<Character>> {
    return remoteDataSource.getCharacter(characterId)
  }

  override fun getCharacters(offset: Int, limit: Int): Single<DataWrapper<Character>> {
    return remoteDataSource.getCharacters(offset, limit)
  }

  override fun getCharacters(nameStartsWith: String): Single<DataWrapper<Character>> {
    return remoteDataSource.getCharacters(nameStartsWith)
  }

  override fun getCharacterComics(characterId: Int): Single<DataWrapper<Comic>> {
    return remoteDataSource.getCharacterComics(characterId)
  }

  override fun getCharacterEvents(characterId: Int): Single<DataWrapper<Event>> {
    return remoteDataSource.getCharacterEvents(characterId)
  }

  override fun getCharacterSeries(characterId: Int): Single<DataWrapper<Series>> {
    return remoteDataSource.getCharacterSeries(characterId)
  }

  override fun getCharacterStories(characterId: Int): Single<DataWrapper<Story>> {
    return remoteDataSource.getCharacterStories(characterId)
  }
}
package io.github.sithengineer.marvelcharacters.data.source

import io.github.sithengineer.marvelcharacters.data.model.CharacterDataWrapper
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

  override fun getCharacters(offset: Int, limit: Int): Single<CharacterDataWrapper> {
    return remoteDataSource.getCharacters(offset, limit)
  }
}
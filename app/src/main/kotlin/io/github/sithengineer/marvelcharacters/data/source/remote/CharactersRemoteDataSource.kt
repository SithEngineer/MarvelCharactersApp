package io.github.sithengineer.marvelcharacters.data.source.remote

import io.github.sithengineer.marvelcharacters.data.model.CharacterDataWrapper
import io.github.sithengineer.marvelcharacters.data.source.CharactersDataSource
import io.github.sithengineer.marvelcharacters.util.HashUtils
import io.reactivex.Single

class CharactersRemoteDataSource(private val api: MarvelCharactersApi,
    private val publicApiKey: String, private val privateApiKey: String) : CharactersDataSource {

  override fun getCharacters(offset: Int, limit: Int): Single<CharacterDataWrapper> {
    /*
    Server-side applications must pass two parameters in addition to the apikey parameter:
      ts - a timestamp (or other long string which can change on a request-by-request basis)
      hash - a md5 digest of the ts parameter, your private key and your public key (e.g. md5(ts+privateKey+publicKey)
    */
    val timestamp = System.currentTimeMillis().toString(10)
    val keyHash = HashUtils.md5("$timestamp$privateApiKey$publicApiKey")
    return api.getCharacters(offset, limit, publicApiKey, timestamp, keyHash)
  }

}
package io.github.sithengineer.marvelcharacters.data

interface CharactersRepository {
  private companion object {
    val API_URI = "/v1/public/"
    val CHARACTERS = "characters"
  }
}
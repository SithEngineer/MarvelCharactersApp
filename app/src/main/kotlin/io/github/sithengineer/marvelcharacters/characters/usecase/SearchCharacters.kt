package io.github.sithengineer.marvelcharacters.characters.usecase

import io.github.sithengineer.marvelcharacters.UseCase
import io.github.sithengineer.marvelcharacters.characters.usecase.filter.CharacterFilter
import io.github.sithengineer.marvelcharacters.data.model.Character
import io.github.sithengineer.marvelcharacters.data.source.CharactersRepository
import io.reactivex.Single

class SearchCharacters(
    private val charactersRepository: CharactersRepository,
    private val filter: CharacterFilter) : UseCase<SearchCharacters.Request, SearchCharacters.Response> {

  override fun execute(request: Request): Response {
    val rxResponse = charactersRepository.getCharacters(request.nameStartsWith)
    return Response(rxResponse.map { it -> it.data?.results?.let { filter.filter(it) } })
  }

  class Request(val nameStartsWith: String) : UseCase.RequestValues

  class Response(val characters: Single<List<Character>>) : UseCase.ResponseValue
}
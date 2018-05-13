package io.github.sithengineer.marvelcharacters.usecase

import io.github.sithengineer.marvelcharacters.usecase.filter.CharacterFilter
import io.github.sithengineer.marvelcharacters.data.source.apimodel.Character
import io.github.sithengineer.marvelcharacters.data.source.CharactersRepository
import io.github.sithengineer.marvelcharacters.usecase.SearchCharacters.Request
import io.github.sithengineer.marvelcharacters.usecase.SearchCharacters.Response
import io.reactivex.Single

class SearchCharacters(
    private val charactersRepository: CharactersRepository,
    private val filter: CharacterFilter) :
    UseCase<Request, Response> {

  override fun execute(request: Request): Response {
    val rxResponse = charactersRepository.getCharacters(request.nameStartsWith)
    return Response(
        rxResponse.map { it -> it.data.results?.let { filter.filter(it) } })
  }

  class Request(val nameStartsWith: String) : UseCase.RequestValues

  class Response(val characters: Single<List<Character>>) : UseCase.ResponseValue
}
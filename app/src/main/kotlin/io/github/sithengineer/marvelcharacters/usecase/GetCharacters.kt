package io.github.sithengineer.marvelcharacters.usecase

import io.github.sithengineer.marvelcharacters.usecase.filter.CharacterFilter
import io.github.sithengineer.marvelcharacters.data.source.apimodel.Character
import io.github.sithengineer.marvelcharacters.data.source.CharactersRepository
import io.github.sithengineer.marvelcharacters.usecase.GetCharacters.Request
import io.github.sithengineer.marvelcharacters.usecase.GetCharacters.Response
import io.reactivex.Single

class GetCharacters(
    private val charactersRepository: CharactersRepository,
    private val filter: CharacterFilter) :
    UseCase<Request, Response> {

  override fun execute(request: Request): Response {
    val rxResponse = charactersRepository.getCharacters(request.offset, request.limit)
    return Response(
        rxResponse.map { it -> it.data.results?.let { filter.filter(it) } })
  }

  class Request(val offset: Int, val limit: Int) : UseCase.RequestValues

  class Response(val characters: Single<List<Character>>) : UseCase.ResponseValue
}
package io.github.sithengineer.marvelcharacters.characters.usecase

import io.github.sithengineer.marvelcharacters.UseCase
import io.github.sithengineer.marvelcharacters.characters.filter.CharacterFilter
import io.github.sithengineer.marvelcharacters.data.model.Character
import io.github.sithengineer.marvelcharacters.data.source.CharactersRepository
import io.reactivex.Single

class GetCharacters(
    private val charactersRepository: CharactersRepository,
    private val filter: CharacterFilter) : UseCase<GetCharacters.Request, GetCharacters.Response> {

  override fun execute(request: Request): Response {
    val rxResponse = charactersRepository.getCharacters(request.offset)
    return Response( rxResponse.map { it -> filter.filter(it.data.results) } )
  }

  class Request(val offset: Int) : UseCase.RequestValues

  class Response(val characters: Single<List<Character>>) : UseCase.ResponseValue
}
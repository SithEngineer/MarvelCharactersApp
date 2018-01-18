package io.github.sithengineer.marvelcharacters.characterdetails.usecase

import io.github.sithengineer.marvelcharacters.UseCase
import io.github.sithengineer.marvelcharacters.data.model.*
import io.github.sithengineer.marvelcharacters.data.source.CharactersRepository
import io.reactivex.Single

class GetSpecificCharacter(
    private val charactersRepository: CharactersRepository)
  : UseCase<GetSpecificCharacter.Request, GetSpecificCharacter.Response> {

  override fun execute(request: Request): Response {
    val character = charactersRepository.getCharacter(request.characterId)
    val characterComics = charactersRepository.getCharacterComics(request.characterId)
    val characterEvents = charactersRepository.getCharacterEvents(request.characterId)
    val characterSeries = charactersRepository.getCharacterSeries(request.characterId)
    val characterStories = charactersRepository.getCharacterStories(request.characterId)
    return Response(
        character = character.map { wrapper -> wrapper.data.results?.first() },
        comics = characterComics.map { wrapper -> wrapper.data.results },
        events = characterEvents.map { wrapper -> wrapper.data.results },
        series = characterSeries.map { wrapper -> wrapper.data.results },
        stories = characterStories.map { wrapper -> wrapper.data.results }
    )
  }

  class Request(val characterId: Int) : UseCase.RequestValues

  class Response(
      val character: Single<Character>,
      val comics: Single<List<Comic>>,
      val events: Single<List<Event>>,
      val series: Single<List<Series>>,
      val stories: Single<List<Story>>
  ) : UseCase.ResponseValue
}


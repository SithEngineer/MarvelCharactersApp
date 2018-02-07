package io.github.sithengineer.marvelcharacters

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import io.github.sithengineer.marvelcharacters.data.source.apimodel.*
import io.github.sithengineer.marvelcharacters.data.source.CharactersDataSource
import io.reactivex.rxkotlin.toSingle
import java.time.Instant
import java.util.*

object DummyData {

  fun getMockedRemoteDataSource(characterId: Int): CharactersDataSource {
    return mock<CharactersDataSource> {
      on {
        getCharacters(limit = any(), offset = any())
      } doReturn giveCharactersWrapper().toSingle()
      on {
        getCharacters(nameStartsWith = any())
      } doReturn giveCharactersWrapper().toSingle()
      on { getCharacter(characterId) } doReturn giveCharactersWrapper(
          characterId).toSingle()
      on {
        getCharacterComics(characterId)
      } doReturn giveComicsWrapper().toSingle()
      on {
        getCharacterEvents(characterId)
      } doReturn giveEventsWrapper().toSingle()
      on {
        getCharacterSeries(characterId)
      } doReturn giveSeriesWrapper().toSingle()
      on {
        getCharacterStories(characterId)
      } doReturn giveStoriesWrapper().toSingle()
    }
  }

  fun <T> giveWrapperFor(data: List<T>): DataWrapper<T> {
    val dataContainer = DataContainer(0, 0, 0, 0, data)
    return DataWrapper(200, "OK", "", "", "", "", dataContainer)
  }

  //
  // Comics
  //

  fun giveComicsWrapper(): DataWrapper<Comic> = giveWrapperFor(givenComics())

  fun givenComics(): MutableList<Comic> {
    val list: MutableList<Comic> = mutableListOf()
    for (i in 0..9) {
      list.add(i, givenComic(i))
    }
    return list
  }

  fun givenComic(id: Int): Comic {
    return Comic(id = id, title = "story $id", description = "story description $id",
        thumbnail = Image("", ""))
  }

  //
  // Events
  //

  fun giveEventsWrapper(): DataWrapper<Event> = giveWrapperFor(givenEvents())

  fun givenEvents(): MutableList<Event> {
    val list: MutableList<Event> = mutableListOf()
    for (i in 0..9) {
      list.add(i, givenEvent(i))
    }
    return list
  }

  fun givenEvent(id: Int): Event {
    return Event(id = id, title = "story $id", description = "story description $id",
        thumbnail = Image("", ""))
  }

  //
  // Series
  //

  fun giveSeriesWrapper(): DataWrapper<Series> = giveWrapperFor(givenSeries())

  fun givenSeries(): MutableList<Series> {
    val list: MutableList<Series> = mutableListOf()
    for (i in 0..9) {
      list.add(i, givenSeries(i))
    }
    return list
  }

  fun givenSeries(id: Int): Series {
    return Series(id = id, title = "story $id", description = "story description $id",
        thumbnail = Image("", ""))
  }

  //
  // Stories
  //

  fun giveStoriesWrapper(): DataWrapper<Story> = giveWrapperFor(givenStories())

  fun givenStories(): MutableList<Story> {
    val list: MutableList<Story> = mutableListOf()
    for (i in 0..9) {
      list.add(i, givenStory(i))
    }
    return list
  }

  fun givenStory(id: Int): Story {
    return Story(id = id, title = "story $id", description = "story description $id",
        thumbnail = Image("", ""))
  }

  //
  // Characters
  //

  fun giveCharactersWrapper(): DataWrapper<Character> = giveWrapperFor(givenCharacters())
  fun giveCharactersWrapper(characterId: Int): DataWrapper<Character> = giveWrapperFor(
      givenCharacters(characterId))

  fun givenCharacters(): MutableList<Character> {
    val list: MutableList<Character> = mutableListOf()
    for (i in 0..9) {
      list.add(i, givenCharacter(i))
    }
    return list
  }

  fun givenCharacters(characterId: Int): MutableList<Character> = arrayListOf(
      givenCharacter(characterId))

  fun givenCharacter(i: Int): Character {
    val emptyMarvelListComicSummary = MarvelList<ComicSummary>(0, 0, "", emptyList())
    val emptyMarvelListStorySummary = MarvelList<StorySummary>(0, 0, "", emptyList())
    val emptyMarvelListEventSummary = MarvelList<EventSummary>(0, 0, "", emptyList())
    val emptyMarvelListSeriesSummary = MarvelList<SeriesSummary>(0, 0, "", emptyList())

    return Character(i, name = "name $i", description = "description $i", modified = Date.from(
        Instant.EPOCH), thumbnail = Image("", ""), resourceURI = "",
        comics = emptyMarvelListComicSummary, urls = emptyList(),
        stories = emptyMarvelListStorySummary, events = emptyMarvelListEventSummary,
        series = emptyMarvelListSeriesSummary)
  }

}
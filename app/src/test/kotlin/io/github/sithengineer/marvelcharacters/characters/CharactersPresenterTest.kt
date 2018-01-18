package io.github.sithengineer.marvelcharacters.characters

import com.nhaarman.mockito_kotlin.*
import io.github.sithengineer.marvelcharacters.characters.usecase.filter.EmptyFilter
import io.github.sithengineer.marvelcharacters.characters.usecase.GetCharacters
import io.github.sithengineer.marvelcharacters.data.model.*
import io.github.sithengineer.marvelcharacters.data.source.CharactersDataSource
import io.github.sithengineer.marvelcharacters.data.source.CharactersRepository
import io.reactivex.BackpressureStrategy
import io.reactivex.rxkotlin.toSingle
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.jetbrains.spek.api.dsl.xit
import java.time.Instant
import java.util.*

class CharactersPresenterTest : Spek({
  given("a presenter for the list of characters view") {

    val publisherCharacterSelected = PublishSubject.create<Character>()
    val view = mock<CharactersContract.View> {
      on { characterSelected() } doReturn publisherCharacterSelected.toFlowable(
          BackpressureStrategy.DROP)
    }

    val charactersRemoteDataSource = mock<CharactersDataSource> {
      on { getCharacters(any(), any()) } doReturn giveCharactersWrapper().toSingle()
    }
    //val charactersRepository = CharactersRepository.getInstance(charactersRemoteDataSource)
    val charactersRepository = CharactersRepository(charactersRemoteDataSource)

    val getCharactersUseCase = GetCharacters(charactersRepository, EmptyFilter())
    val charactersPresenter = CharactersPresenter(view, getCharactersUseCase,
        Schedulers.trampoline(), Schedulers.trampoline())

    on("characters view created") {
      it("should have the presenter set") {
        verify(view, times(1)).setPresenter(charactersPresenter)
      }
    }

    on("presenter started") {
      charactersPresenter.start()
      it("should load first batch of characters") {
        verify(charactersRemoteDataSource, times(1)).getCharacters(0, 10)
      }

      it("should set first list of characters in the view") {
        verify(view, times(1)).showBigCenteredLoading()
        verify(view, times(1)).showCharacters(givenCharacters())
        verify(view, times(1)).hideLoading()
      }
    }

    on("scrolled to bottom") {
      xit("should attempt to load more characters") {
        // TODO
      }
    }

    on("character selected") {
      publisherCharacterSelected.onNext(getCharacter(0))
      it("should navigate to character details view") {
        verify(view, times(1)).showCharacterDetails(0)
      }
    }
  }
})

private fun giveCharactersWrapper(): DataWrapper {
  val characters: MutableList<Character> = givenCharacters()
  val dataContainer = DataContainer(0, 0, 0, 0, characters)
  return DataWrapper(200, "OK", "", "", "", "", dataContainer)
}

private fun givenCharacters(): MutableList<Character> {
  val characters: MutableList<Character> = mutableListOf()
  for (i in 0..9) {
    characters.add(i, getCharacter(i))
  }
  return characters
}

private fun getCharacter(i: Int): Character {
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

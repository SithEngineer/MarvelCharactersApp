package io.github.sithengineer.marvelcharacters.characters

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import io.github.sithengineer.marvelcharacters.DummyData
import io.github.sithengineer.marvelcharacters.data.source.apimodel.Character
import io.github.sithengineer.marvelcharacters.data.source.CharactersRepository
import io.github.sithengineer.marvelcharacters.presentation.characters.CharactersContract
import io.github.sithengineer.marvelcharacters.presentation.characters.CharactersPresenter
import io.github.sithengineer.marvelcharacters.usecase.GetCharacters
import io.github.sithengineer.marvelcharacters.usecase.SearchCharacters
import io.github.sithengineer.marvelcharacters.usecase.filter.EmptyFilter
import io.github.sithengineer.marvelcharacters.usecase.filter.LimitFilter
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

class CharactersPresenterTest : Spek({
  given("a presenter for the list of characters view") {

    val maximumSearchResults = 2
    val characterId = 2
    val publisherCharacterSelected = PublishSubject.create<Character>()
    val publisherScrolledToBottomWithOffset = PublishSubject.create<Int>()
    val publisherSearchedForTerm = PublishSubject.create<String>()
    val publisherSearchItemPressedWithId = PublishSubject.create<Int>()

    val view = mock<CharactersContract.View> {
      on { onCharacterSelected() } doReturn publisherCharacterSelected
      on { onScrolledToBottomWithOffset() } doReturn publisherScrolledToBottomWithOffset
      on { onSearchedForTerm() } doReturn publisherSearchedForTerm
      on { onSearchedItemPressedWithId() } doReturn publisherSearchItemPressedWithId
    }

    val charactersRemoteDataSource = DummyData.getMockedRemoteDataSource(characterId)
    val charactersRepository = CharactersRepository(charactersRemoteDataSource)

    val getCharactersUseCase = GetCharacters(
        charactersRepository, EmptyFilter())
    val searchCharacters = SearchCharacters(
        charactersRepository, LimitFilter(maximumSearchResults)
    )
    val charactersPresenter = CharactersPresenter(
        view, getCharactersUseCase, searchCharacters,
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
        verify(view, times(1)).showCharacters(DummyData.givenCharacters())
        verify(view, times(1)).hideLoading()
      }
    }

    on("scrolled to bottom") {
      publisherScrolledToBottomWithOffset.onNext(10)
      it("should attempt to load more characters") {
        verify(charactersRemoteDataSource, times(1)).getCharacters(10, 10)
      }
    }

    on("character selected") {
      publisherCharacterSelected.onNext(DummyData.givenCharacter(0))
      it("should navigate to character details view") {
        verify(view, times(1)).showCharacterDetails(0)
      }
    }

    on("searched for term") {
      publisherSearchedForTerm.onNext("name")
      it("should show a list of top $maximumSearchResults search results") {
        verify(view, times(1)).showSearchResult(DummyData.givenCharacters().take(maximumSearchResults))
      }
    }

    on("pressed search item"){
      publisherSearchItemPressedWithId.onNext(0)
      it("should navigate to character details view") {
        // this method should be invoked by the second time at this point
        verify(view, times(2)).showCharacterDetails(0)
      }
    }
  }
})

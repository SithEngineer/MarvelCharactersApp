package io.github.sithengineer.marvelcharacters.comicbookcovers

import com.nhaarman.mockito_kotlin.*
import io.github.sithengineer.marvelcharacters.DummyData
import io.github.sithengineer.marvelcharacters.data.source.CharactersRepository
import io.github.sithengineer.marvelcharacters.presentation.comicbookcovers.ComicBookCoversContract
import io.github.sithengineer.marvelcharacters.presentation.comicbookcovers.ComicBookCoversPresenter
import io.github.sithengineer.marvelcharacters.usecase.GetSpecificCharacterDetails
import io.github.sithengineer.marvelcharacters.presentation.viewmodel.ComicBookType
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

class ComicBookCoversPresenterTest : Spek({
  given("a presenter for the list of a character comic book covers view") {

    val characterId = 2
    val comicBookType = ComicBookType.COMIC
    val publisherExitSelected = PublishSubject.create<Any>()

    val view = mock<ComicBookCoversContract.View> {
      on { onSelectedExit() } doReturn publisherExitSelected
    }

    val charactersRemoteDataSource = DummyData.getMockedRemoteDataSource(characterId)
    val charactersRepository = CharactersRepository(charactersRemoteDataSource)

    val getCharactersUseCase = GetSpecificCharacterDetails(charactersRepository)
    val charactersPresenter = ComicBookCoversPresenter(
        view, getCharactersUseCase,
        Schedulers.trampoline(), Schedulers.trampoline(), characterId, comicBookType)

    on("comic book covers view created") {
      it("should have the presenter set") {
        verify(view, times(1)).setPresenter(charactersPresenter)
      }
    }

    on("presenter started") {
      charactersPresenter.start()
      it("should load detailed character info") {
        verify(view, times(1)).showLoading()
        verify(view, times(1)).hideLoading()
        // TODO validate exactly which comic books are being shown
        verify(view, times(1)).showBookCovers(any())
      }
    }

    on("selected exit"){
      publisherExitSelected.onNext(Any())
      it("should navigate back"){
        verify(view, times(1)).navigateBack()
      }
    }
  }
})
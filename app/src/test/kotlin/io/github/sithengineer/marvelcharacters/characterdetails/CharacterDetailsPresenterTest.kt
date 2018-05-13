package io.github.sithengineer.marvelcharacters.characterdetails

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import io.github.sithengineer.marvelcharacters.DummyData
import io.github.sithengineer.marvelcharacters.data.source.CharactersRepository
import io.github.sithengineer.marvelcharacters.presentation.characterdetails.CharacterDetailsContract
import io.github.sithengineer.marvelcharacters.presentation.characterdetails.CharacterDetailsPresenter
import io.github.sithengineer.marvelcharacters.usecase.GetSpecificCharacterDetails
import io.github.sithengineer.marvelcharacters.presentation.viewmodel.ComicBook
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

class CharacterDetailsPresenterTest : Spek({
  given("a presenter for the details of a character view") {

    val characterId = 2
    val publisherComicBookSelected = PublishSubject.create<ComicBook>()
    val publisherEventsBookSelected = PublishSubject.create<ComicBook>()
    val publisherSeriesBookSelected = PublishSubject.create<ComicBook>()
    val publisherStoriesBookSelected = PublishSubject.create<ComicBook>()

    val publisherRelatedLinkComicDetailClick = PublishSubject.create<String>()
    val publisherRelatedLinkDetailClick = PublishSubject.create<String>()
    val publisherRelatedLinkWikiClick = PublishSubject.create<String>()

    val view = mock<CharacterDetailsContract.View> {
      on { onSelectedComicBook() } doReturn publisherComicBookSelected
      on { onSelectedEventsBook() } doReturn publisherEventsBookSelected
      on { onSelectedSeriesBook() } doReturn publisherSeriesBookSelected
      on { onSelectedStoriesBook() } doReturn publisherStoriesBookSelected
      on { onSelectedRelatedLinksComicLink() } doReturn publisherRelatedLinkComicDetailClick
      on { onSelectedRelatedLinksDetail() } doReturn publisherRelatedLinkDetailClick
      on { onSelectedRelatedLinksWiki() } doReturn publisherRelatedLinkWikiClick
    }

    val charactersRemoteDataSource = DummyData.getMockedRemoteDataSource(characterId)
    val charactersRepository = CharactersRepository(charactersRemoteDataSource)

    val getCharactersUseCase = GetSpecificCharacterDetails(charactersRepository)
    val charactersPresenter = CharacterDetailsPresenter(
        view, getCharactersUseCase,
        Schedulers.trampoline(), Schedulers.trampoline(), characterId)

    on("character details view created") {
      it("should have the presenter set") {
        verify(view, times(1)).setPresenter(charactersPresenter)
      }
    }

    on("presenter started") {
      charactersPresenter.start()
      it("should load detailed character info") {
        verify(view, times(1)).showCharacterDetails(DummyData.givenCharacter(characterId))
      }
      it("should load comics"){
        verify(view, times(1)).showCharacterComics(DummyData.givenComics())
      }
      it("should load events"){
        verify(view, times(1)).showCharacterEvents(DummyData.givenEvents())
      }
      it("should load series"){
        verify(view, times(1)).showCharacterSeries(DummyData.givenSeries())
      }
      it("should load stories"){
        verify(view, times(1)).showCharacterStories(DummyData.givenStories())
      }
    }
  }
})
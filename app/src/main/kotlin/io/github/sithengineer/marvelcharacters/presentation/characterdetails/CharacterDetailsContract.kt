package io.github.sithengineer.marvelcharacters.presentation.characterdetails

import io.github.sithengineer.marvelcharacters.presentation.BasePresenter
import io.github.sithengineer.marvelcharacters.presentation.BaseView
import io.github.sithengineer.marvelcharacters.data.source.apimodel.*
import io.github.sithengineer.marvelcharacters.presentation.viewmodel.ComicBook
import io.github.sithengineer.marvelcharacters.presentation.viewmodel.ComicBookType
import io.reactivex.Observable

interface CharacterDetailsContract {
  interface View : BaseView<Presenter> {
    fun showLoading()
    fun hideLoading()

    fun onSelectedRelatedLinksDetail(): Observable<String>
    fun onSelectedRelatedLinksComicLink(): Observable<String>
    fun onSelectedRelatedLinksWiki(): Observable<String>
    fun onSelectedComicBook(): Observable<ComicBook>
    fun onSelectedSeriesBook(): Observable<ComicBook>
    fun onSelectedEventsBook(): Observable<ComicBook>
    fun onSelectedStoriesBook(): Observable<ComicBook>

    fun showCharacterDetails(character: Character)
    fun showCharacterComics(comics: List<Comic>)
    fun showCharacterEvents(events: List<Event>)
    fun showCharacterSeries(series: List<Series>)
    fun showCharacterStories(stories: List<Story>)
    fun showComicsCovers(characterId: Int, comicBookType: ComicBookType)
    fun showUrl(url: String)
  }

  interface Presenter : BasePresenter
}
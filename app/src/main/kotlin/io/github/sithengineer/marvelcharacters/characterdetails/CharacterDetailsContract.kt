package io.github.sithengineer.marvelcharacters.characterdetails

import io.github.sithengineer.marvelcharacters.BasePresenter
import io.github.sithengineer.marvelcharacters.BaseView
import io.github.sithengineer.marvelcharacters.data.model.*
import io.github.sithengineer.marvelcharacters.viewmodel.ComicBook
import io.github.sithengineer.marvelcharacters.viewmodel.ComicBookType
import io.reactivex.Observable

interface CharacterDetailsContract {
  interface View : BaseView<Presenter> {
    fun showLoading()
    fun hideLoading()

    fun selectedRelatedLinksDetail(): Observable<String>
    fun selectedRelatedLinksComicLink(): Observable<String>
    fun selectedRelatedLinksWiki(): Observable<String>
    fun selectedComicBook(): Observable<ComicBook>

    fun showCharacterDetails(character: Character)
    fun showCharacterComics(comics: List<Comic>)
    fun showCharacterEvents(events: List<Event>)
    fun showCharacterSeries(series: List<Series>)
    fun showCharacterStories(stories: List<Story>)
    fun showComicsCovers(characterId: Int, comicBookType: ComicBookType)
    fun showUrl(url: String)
    fun selectedSeriesBook(): Observable<ComicBook>
    fun selectedEventsBook(): Observable<ComicBook>
    fun selectedStoriesBook(): Observable<ComicBook>
  }

  interface Presenter : BasePresenter
}
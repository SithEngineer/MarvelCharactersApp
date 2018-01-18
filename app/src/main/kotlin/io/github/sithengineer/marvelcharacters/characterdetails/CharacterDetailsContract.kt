package io.github.sithengineer.marvelcharacters.characterdetails

import io.github.sithengineer.marvelcharacters.BasePresenter
import io.github.sithengineer.marvelcharacters.BaseView
import io.github.sithengineer.marvelcharacters.data.model.*
import io.reactivex.Observable

interface CharacterDetailsContract {
  interface View : BaseView<Presenter> {
    fun showLoading()
    fun hideLoading()

    fun selectedRelatedLinksDetail(): Observable<String>
    fun selectedRelatedLinksComicLink(): Observable<String>
    fun selectedRelatedLinksWiki(): Observable<String>

    fun showCharacterDetails(character: Character)
    fun showCharacterComics(comics: List<Comic>)
    fun showCharacterEvents(events: List<Event>)
    fun showCharacterSeries(series: List<Series>)
    fun showCharacterStories(stories: List<Story>)
  }

  interface Presenter : BasePresenter
}
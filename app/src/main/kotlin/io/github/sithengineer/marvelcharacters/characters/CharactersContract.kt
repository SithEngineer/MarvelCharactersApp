package io.github.sithengineer.marvelcharacters.characters

import io.github.sithengineer.marvelcharacters.BasePresenter
import io.github.sithengineer.marvelcharacters.BaseView
import io.github.sithengineer.marvelcharacters.data.model.Character
import io.reactivex.Observable

interface CharactersContract {
  interface View : BaseView<Presenter> {
    fun showBigCenteredLoading()
    fun showSmallBottomLoading()
    fun hideLoading()
    fun showCharacters(characters: List<Character>)
    fun characterSelected(): Observable<Character>
    fun showCharacterDetails(characterId: Int)
    fun scrolledToBottomWithOffset(): Observable<Int>
    fun searchedItemPressedWithId(): Observable<Int>
    fun searchedForTerm(): Observable<String>
    fun showSearchResult(characters: List<Character>)
  }

  interface Presenter : BasePresenter
}
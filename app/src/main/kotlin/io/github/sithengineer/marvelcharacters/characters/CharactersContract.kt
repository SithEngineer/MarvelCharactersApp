package io.github.sithengineer.marvelcharacters.characters

import io.github.sithengineer.marvelcharacters.BasePresenter
import io.github.sithengineer.marvelcharacters.BaseView
import io.github.sithengineer.marvelcharacters.data.source.apimodel.Character
import io.reactivex.Observable

interface CharactersContract {
  interface View : BaseView<Presenter> {
    fun showBigCenteredLoading()
    fun showSmallBottomLoading()
    fun hideLoading()

    fun showCharacters(characters: List<Character>)
    fun showCharacterDetails(characterId: Int)
    fun showSearchResult(characters: List<Character>)

    fun onCharacterSelected(): Observable<Character>
    fun onScrolledToBottomWithOffset(): Observable<Int>
    fun onSearchedItemPressedWithId(): Observable<Int>
    fun onSearchedForTerm(): Observable<String>
  }

  interface Presenter : BasePresenter
}
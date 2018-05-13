package io.github.sithengineer.marvelcharacters.presentation.characters

import io.github.sithengineer.marvelcharacters.presentation.BasePresenter
import io.github.sithengineer.marvelcharacters.presentation.BaseView
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
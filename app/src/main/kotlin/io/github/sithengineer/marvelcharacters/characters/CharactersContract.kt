package io.github.sithengineer.marvelcharacters.characters

import io.github.sithengineer.marvelcharacters.BasePresenter
import io.github.sithengineer.marvelcharacters.BaseView
import io.github.sithengineer.marvelcharacters.data.model.Character
import io.reactivex.Flowable

interface CharactersContract {
  interface View : BaseView<Presenter> {
    fun showLoading()
    fun hideLoading()
    fun showCharacters(characters: List<Character>)
    fun characterSelected(): Flowable<Character>
    fun showCharacterDetails(characterId: Int)
  }

  interface Presenter : BasePresenter
}
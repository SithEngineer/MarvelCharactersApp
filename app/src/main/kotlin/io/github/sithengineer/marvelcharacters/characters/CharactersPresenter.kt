package io.github.sithengineer.marvelcharacters.characters

import io.github.sithengineer.marvelcharacters.characters.usecase.GetCharacters
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class CharactersPresenter(
    private val view: CharactersContract.View,
    private val charactersUseCase: GetCharacters) : CharactersContract.Presenter {

  private val compositeSubscription: CompositeDisposable

  init {
    view.setPresenter(this)
    compositeSubscription = CompositeDisposable()
  }

  override fun start() {
    loadCharacters()
    handleCharacterSelected()
  }

  private fun handleCharacterSelected() {
    compositeSubscription.add(
        view.characterSelected().subscribe { character -> view.showCharacterDetails(character.id) }
    )
  }

  private fun loadCharacters() {
    view.showLoading()
    val request = GetCharacters.Request(0)
    val response = charactersUseCase.execute(request)
    compositeSubscription.add(
        response.characters
            .doOnError { err ->
              view.hideLoading()
              Timber.e(err)
            }
            .subscribe { characters ->
              view.showCharacters(characters)
              view.hideLoading()
              Timber.d("showing characters")
            }
    )
  }

}
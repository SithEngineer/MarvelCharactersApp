package io.github.sithengineer.marvelcharacters.characters

import io.github.sithengineer.marvelcharacters.characters.usecase.GetCharacters
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class CharactersPresenter(
    private val view: CharactersContract.View,
    private val charactersUseCase: GetCharacters,
    private val ioScheduler: Scheduler,
    private val viewScheduler: Scheduler) : CharactersContract.Presenter {

  private lateinit var compositeSubscription: CompositeDisposable

  private var charactersOffset = 0

  private companion object {
    val CHARACTERS_FETCH_LIMIT = 10
  }

  init {
    view.setPresenter(this)
  }

  override fun start() {
    Timber.d("presenter started")
    compositeSubscription = CompositeDisposable()
    loadCharacters(isFirstBatch = true)
    handleCharacterSelected()
    handleCharactersListReachedBottom()
  }

  override fun stop() {
    if (!compositeSubscription.isDisposed) {
      compositeSubscription.dispose()
    }
    Timber.d("presenter stopped")
  }


  private fun handleCharactersListReachedBottom() {
    compositeSubscription.add(
        view.scrolledToBottomWithOffset().observeOn(viewScheduler).subscribe({ offset ->
          Timber.d("reached bottom with offset = $offset")
          loadCharacters()
        }))
  }

  private fun handleCharacterSelected() {
    compositeSubscription.add(
        view.characterSelected().observeOn(viewScheduler).subscribe { character ->
          Timber.d("Character selected: $character")
          character.id?.let {
            view.showCharacterDetails(it)
          }
        }
    )
  }

  private fun loadCharacters(isFirstBatch: Boolean = false) {
    Timber.d("loading characters")
    if (isFirstBatch) {
      view.showBigCenteredLoading()
    } else {
      view.showSmallBottomLoading()
    }
    val request = GetCharacters.Request(charactersOffset, CHARACTERS_FETCH_LIMIT)
    val response = charactersUseCase.execute(request)
    compositeSubscription.add(
        response.characters
            .subscribeOn(ioScheduler)
            .observeOn(viewScheduler)
            .subscribe({ characters ->
              view.showCharacters(characters)
              charactersOffset += characters.size
              view.hideLoading()
              Timber.d("showing characters")
            }, { err ->
              view.hideLoading()
              Timber.e(err)
            })
    )
  }

}
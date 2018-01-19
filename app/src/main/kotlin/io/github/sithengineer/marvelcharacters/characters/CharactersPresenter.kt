package io.github.sithengineer.marvelcharacters.characters

import io.github.sithengineer.marvelcharacters.usecase.GetCharacters
import io.github.sithengineer.marvelcharacters.usecase.SearchCharacters
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class CharactersPresenter(
    private val view: CharactersContract.View,
    private val getCharactersUseCase: GetCharacters,
    private val searchCharactersUseCase: SearchCharacters,
    private val ioScheduler: Scheduler,
    private val viewScheduler: Scheduler) : CharactersContract.Presenter {

  private lateinit var compositeSubscription: CompositeDisposable

  private var charactersOffset = 0

  private companion object {
    const val CHARACTERS_FETCH_LIMIT = 10
  }

  init {
    view.setPresenter(this)
  }

  override fun stop() {
    if (!compositeSubscription.isDisposed) {
      compositeSubscription.dispose()
    }
    Timber.d("${this.javaClass.name}::stopped()")
  }

  override fun start() {
    Timber.d("${this.javaClass.name}::started()")
    compositeSubscription = CompositeDisposable()
    loadCharacters(isFirstBatch = true)
    handleCharacterSelected()
    handleCharactersListReachedBottom()
    handleSearchTerms()
    handleSearchItemPressed()
  }


  private fun handleCharactersListReachedBottom() {
    compositeSubscription.add(
        view.onScrolledToBottomWithOffset().observeOn(viewScheduler).subscribe({ offset ->
          Timber.d("reached bottom with offset = $offset")
          loadCharacters()
        }))
  }

  private fun handleCharacterSelected() {
    compositeSubscription.add(
        view.onCharacterSelected().observeOn(viewScheduler).subscribe { character ->
          Timber.d("Character selected: $character")
          character.id?.let {
            view.showCharacterDetails(it)
          }
        }
    )
  }

  private fun handleSearchItemPressed() {
    compositeSubscription.add(
        view.onSearchedItemPressedWithId()
            .subscribeOn(viewScheduler)
            .subscribe({ characterId ->
              view.showCharacterDetails(characterId)
            }, { err ->
              Timber.e(err)
            })
    )
  }

  private fun handleSearchTerms() {
    compositeSubscription.add(
        view.onSearchedForTerm()
            .doOnNext { query -> Timber.d("searching for: $query") }
            .flatMap { query ->
              val request = SearchCharacters.Request(query)
              searchCharactersUseCase.execute(request).characters.subscribeOn(
                  ioScheduler).toObservable()
            }
            .observeOn(viewScheduler)
            .subscribe({ characters ->
              view.showSearchResult(characters)
            }, { err ->
              Timber.e(err)
            })
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
    val response = getCharactersUseCase.execute(request)
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
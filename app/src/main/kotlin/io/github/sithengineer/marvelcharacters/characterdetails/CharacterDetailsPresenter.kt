package io.github.sithengineer.marvelcharacters.characterdetails

import io.github.sithengineer.marvelcharacters.usecase.GetSpecificCharacterDetails
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class CharacterDetailsPresenter(
    private val view: CharacterDetailsContract.View,
    private val getCharacterDetailsUseCase: GetSpecificCharacterDetails,
    private val ioScheduler: Scheduler,
    private val viewScheduler: Scheduler,
    private val characterId: Int) : CharacterDetailsContract.Presenter {

  private lateinit var compositeSubscription: CompositeDisposable

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
    loadCharacterData()
    handleSelectedComics()
    handleSelectedRelatedLinks()
  }

  private fun handleSelectedRelatedLinks() {
    compositeSubscription.add(
        Observable.merge(arrayListOf(
            view.selectedRelatedLinksComicLink(),
            view.selectedRelatedLinksDetail(),
            view.selectedRelatedLinksWiki()
        )).subscribe({ url ->
          view.showUrl(url)
        }, { err ->
          Timber.e(err)
        })
    )
  }

  private fun handleSelectedComics() {
    compositeSubscription.add(
        Observable.merge(
            arrayListOf(
                view.selectedComicBook(),
                view.selectedEventsBook(),
                view.selectedSeriesBook(),
                view.selectedStoriesBook()
            )
        )
            .observeOn(viewScheduler)
            .subscribe({ comicBook ->
              view.showComicsCovers(characterId, comicBook.comicBookType)
            }, { err -> Timber.e(err) })
    )
  }

  private fun loadCharacterData() {
    view.showLoading()

    val getCharacterResponse = getCharacterDetailsUseCase.execute(
        GetSpecificCharacterDetails.Request(characterId))

    val characterRxResponse = getCharacterResponse
        .character
        .observeOn(viewScheduler)
        .doOnSuccess { character ->
          view.showCharacterDetails(character)
        }

    val comicsRxResponse = getCharacterResponse
        .comics
        .observeOn(viewScheduler)
        .doOnSuccess { comics ->
          view.showCharacterComics(comics)
        }

    val eventsRxResponse = getCharacterResponse
        .events
        .observeOn(viewScheduler)
        .doOnSuccess { events ->
          view.showCharacterEvents(events)
        }

    val seriesRxResponse = getCharacterResponse
        .series
        .observeOn(viewScheduler)
        .doOnSuccess { series ->
          view.showCharacterSeries(series)
        }

    val storiesRxResponse = getCharacterResponse
        .stories
        .observeOn(viewScheduler)
        .doOnSuccess { stories ->
          view.showCharacterStories(stories)
        }

    compositeSubscription.add(
        Single.merge(
            arrayListOf(characterRxResponse, comicsRxResponse, eventsRxResponse, seriesRxResponse,
                storiesRxResponse))
            .subscribeOn(ioScheduler)
            .observeOn(viewScheduler)
            .subscribe(
                { _ ->
                  view.hideLoading()
                },
                { err -> Timber.e(err) }
            )
    )
  }
}
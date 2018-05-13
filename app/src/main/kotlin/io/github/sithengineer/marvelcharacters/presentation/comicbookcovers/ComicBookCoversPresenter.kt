package io.github.sithengineer.marvelcharacters.presentation.comicbookcovers

import io.github.sithengineer.marvelcharacters.data.source.apimodel.Image
import io.github.sithengineer.marvelcharacters.presentation.comicbookcovers.ComicBookCoversContract.Presenter
import io.github.sithengineer.marvelcharacters.presentation.comicbookcovers.ComicBookCoversContract.View
import io.github.sithengineer.marvelcharacters.usecase.GetSpecificCharacterDetails
import io.github.sithengineer.marvelcharacters.presentation.viewmodel.ComicBook
import io.github.sithengineer.marvelcharacters.presentation.viewmodel.ComicBookType
import io.github.sithengineer.marvelcharacters.presentation.viewmodel.ComicBookType.COMIC
import io.github.sithengineer.marvelcharacters.presentation.viewmodel.ComicBookType.EVENT
import io.github.sithengineer.marvelcharacters.presentation.viewmodel.ComicBookType.SERIES
import io.github.sithengineer.marvelcharacters.presentation.viewmodel.ComicBookType.STORY
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class ComicBookCoversPresenter(
    private val view: View,
    private val getCharacterDetailsUseCase: GetSpecificCharacterDetails,
    private val ioScheduler: Scheduler,
    private val viewScheduler: Scheduler,
    private val characterId: Int,
    private val comicBookType: ComicBookType
) : Presenter {

  private lateinit var compositeSubscription: CompositeDisposable

  override fun start() {
    compositeSubscription = CompositeDisposable()
    loadBookCovers()
    handleCloseSelected()
  }

  private fun handleCloseSelected() {
    compositeSubscription.add(
        view.onSelectedExit()
            .observeOn(viewScheduler)
            .subscribe({ _ ->
              view.navigateBack()
            }, { err ->
              Timber.e(err)
            })
    )
  }

  init {
    view.setPresenter(this)
  }

  private fun loadBookCovers() {
    val request = GetSpecificCharacterDetails.Request(characterId)
    val response = getCharacterDetailsUseCase.execute(request)
    val rxData = when (comicBookType) {
      ComicBookType.COMIC -> response.comics.flattenAsFlowable { list -> list }.map { comic ->
        ComicBook(
            imageUrl = getImageUrlFrom(comic.thumbnail), id = comic.id,
            comicBookType = COMIC,
            name = comic.title)
      }.toList()
      ComicBookType.SERIES -> response.series.flattenAsFlowable { list -> list }.map { series ->
        ComicBook(
            imageUrl = getImageUrlFrom(series.thumbnail), id = series.id,
            comicBookType = SERIES,
            name = series.title)
      }.toList()
      ComicBookType.STORY -> response.stories.flattenAsFlowable { list -> list }.map { story ->
        ComicBook(
            imageUrl = getImageUrlFrom(story.thumbnail), id = story.id,
            comicBookType = STORY,
            name = story.title)
      }.toList()
      ComicBookType.EVENT -> response.events.flattenAsFlowable { list -> list }.map { event ->
        ComicBook(
            imageUrl = getImageUrlFrom(event.thumbnail), id = event.id,
            comicBookType = EVENT,
            name = event.title)
      }.toList()
    }

    view.showLoading()
    compositeSubscription.add(
        rxData
            .subscribeOn(ioScheduler)
            .observeOn(viewScheduler)
            .subscribe({ bookCovers ->
              view.hideLoading()
              view.showBookCovers(bookCovers)
            }, { err ->
              Timber.e(err)
            })
    )
  }

  private fun getImageUrlFrom(
      thumbnail: Image?): String = if (thumbnail != null) "${thumbnail.path}.${thumbnail.extension}" else ""

  override fun stop() {
    if (!compositeSubscription.isDisposed) {
      compositeSubscription.dispose()
    }
  }
}
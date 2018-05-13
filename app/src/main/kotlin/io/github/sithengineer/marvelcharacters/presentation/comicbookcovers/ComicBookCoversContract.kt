package io.github.sithengineer.marvelcharacters.presentation.comicbookcovers

import io.github.sithengineer.marvelcharacters.presentation.BasePresenter
import io.github.sithengineer.marvelcharacters.presentation.BaseView
import io.github.sithengineer.marvelcharacters.presentation.viewmodel.ComicBook
import io.reactivex.Observable

interface ComicBookCoversContract {
  interface View : BaseView<Presenter> {
    fun showLoading()
    fun hideLoading()
    fun showBookCovers(bookCovers: List<ComicBook>)
    fun navigateBack()
    fun onSelectedExit(): Observable<Any>
  }

  interface Presenter : BasePresenter
}
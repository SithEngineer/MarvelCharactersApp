package io.github.sithengineer.marvelcharacters.comicbookcovers

import io.github.sithengineer.marvelcharacters.BasePresenter
import io.github.sithengineer.marvelcharacters.BaseView
import io.github.sithengineer.marvelcharacters.viewmodel.ComicBook
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
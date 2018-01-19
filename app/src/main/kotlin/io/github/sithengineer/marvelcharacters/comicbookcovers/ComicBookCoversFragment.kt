package io.github.sithengineer.marvelcharacters.comicbookcovers

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.jakewharton.rxbinding2.view.RxView
import io.github.sithengineer.marvelcharacters.MarvelCharactersApplication
import io.github.sithengineer.marvelcharacters.R
import io.github.sithengineer.marvelcharacters.adapter.BigComicBookAdapter
import io.github.sithengineer.marvelcharacters.usecase.GetSpecificCharacterDetails
import io.github.sithengineer.marvelcharacters.util.DisplayUtils
import io.github.sithengineer.marvelcharacters.util.SpacingItemDecoration
import io.github.sithengineer.marvelcharacters.viewmodel.ComicBook
import io.github.sithengineer.marvelcharacters.viewmodel.ComicBookType
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit

class ComicBookCoversFragment : Fragment(), ComicBookCoversContract.View {

  @BindView(R.id.fragment_comic_book_covers_list)
  lateinit var comicBooks: RecyclerView

  @BindView(R.id.fragment_comic_book_covers_progress)
  lateinit var progress: ProgressBar

  @BindView(R.id.fragment_comic_book_covers_close)
  lateinit var closeView: View

  private lateinit var viewUnBinder: Unbinder
  private lateinit var adapter: BigComicBookAdapter
  private lateinit var presenter: ComicBookCoversContract.Presenter

  private var navigator: ComicBookCoversNavigator? = null

  override fun onAttach(context: Context?) {
    super.onAttach(context)
    try {
      navigator = context as ComicBookCoversNavigator
    } catch (e: ClassCastException) {
      Timber.e(e, "Parent activity must implement ${ComicBookCoversNavigator::class.java.name}")
    }
  }

  override fun onDetach() {
    navigator = null
    super.onDetach()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_comic_book_covers, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewUnBinder = ButterKnife.bind(this, view)
    adapter = BigComicBookAdapter()
    comicBooks.adapter = adapter
    comicBooks.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    val itemDecoration = SpacingItemDecoration(DisplayUtils.convertDpToPixel(context, 14),
        isVertical = false)
    comicBooks.addItemDecoration(itemDecoration)
    // comicBooks.itemAnimator = null TODO

    val comicBookType = ComicBookType.valueOf(arguments?.getString(
        COMIC_BOOK_TYPE)!!)
    attachPresenter(arguments?.getInt(CHARACTER_ID)!!, comicBookType)
  }

  override fun onDestroyView() {
    super.onDestroyView()
    viewUnBinder.unbind()
    super.onDestroyView()
  }

  private fun attachPresenter(characterId: Int, comicBookType: ComicBookType) {
    val charactersRepository = (activity?.application as MarvelCharactersApplication).charactersRepository
    val useCase = GetSpecificCharacterDetails(
        charactersRepository)
    ComicBookCoversPresenter(view = this, getCharacterDetailsUseCase = useCase,
        characterId = characterId,
        comicBookType = comicBookType,
        ioScheduler = Schedulers.io(), viewScheduler = AndroidSchedulers.mainThread())
  }

  override fun setPresenter(presenter: ComicBookCoversContract.Presenter) {
    this.presenter = presenter
  }

  override fun onResume() {
    super.onResume()
    presenter.start()
  }

  override fun onPause() {
    presenter.stop()
    super.onPause()
  }

  override fun showLoading() {
    progress.visibility = View.VISIBLE
  }

  override fun hideLoading() {
    progress.visibility = View.GONE
  }

  override fun navigateBack() {
    navigator?.navigateBack()
  }

  override fun selectedExit(): Observable<Any> {
    return RxView.clicks(closeView).debounce(400, TimeUnit.MILLISECONDS)
  }

  override fun showBookCovers(bookCovers: List<ComicBook>) {
    adapter.setComics(bookCovers)
  }

  companion object {

    private const val CHARACTER_ID = "characterId"
    private const val COMIC_BOOK_TYPE = "comicBookType"

    fun newInstance(characterId: Int, comicBookType: String): ComicBookCoversFragment {
      val fragment = ComicBookCoversFragment()
      val args = Bundle()
      args.putInt(CHARACTER_ID, characterId)
      args.putString(COMIC_BOOK_TYPE, comicBookType)
      fragment.arguments = args
      return fragment
    }
  }

}
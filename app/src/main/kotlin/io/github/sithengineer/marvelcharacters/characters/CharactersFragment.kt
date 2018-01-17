package io.github.sithengineer.marvelcharacters.characters

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
import io.github.sithengineer.marvelcharacters.AppNavigator
import io.github.sithengineer.marvelcharacters.MarvelCharactersApplication
import io.github.sithengineer.marvelcharacters.R
import io.github.sithengineer.marvelcharacters.characters.filter.EmptyFilter
import io.github.sithengineer.marvelcharacters.characters.usecase.GetCharacters
import io.github.sithengineer.marvelcharacters.data.model.Character
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit

class CharactersFragment : Fragment(), CharactersContract.View {

  @BindView(R.id.fragment_characters_list)
  lateinit var characters: RecyclerView

  @BindView(R.id.fragment_characters_loading)
  lateinit var mainCenteredProgressBar: ProgressBar

  @BindView(R.id.fragment_characters_more_loading)
  lateinit var smallBottomProgressBar: ProgressBar

  private lateinit var presenter: CharactersContract.Presenter
  private lateinit var unBinder: Unbinder
  private lateinit var adapter: CharactersAdapter
  private var navigator: AppNavigator? = null

  override fun onAttach(context: Context?) {
    super.onAttach(context)
    try {
      navigator = context as AppNavigator
    } catch (e: ClassCastException) {
      Timber.e(e, "Parent activity must implement ${AppNavigator::class.java.name}")
    }
  }

  override fun onDetach() {
    navigator = null
    super.onDetach()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    return inflater!!.inflate(R.layout.fragment_characters, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    unBinder = ButterKnife.bind(this, view)
    adapter = CharactersAdapter()
    characters.adapter = adapter
    characters.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    attachPresenter()
  }

  override fun onDestroyView() {
    unBinder.unbind()
    super.onDestroyView()
  }

  private fun attachPresenter() {
    // fetch and build dependencies
    val charRepo = (activity?.application as MarvelCharactersApplication).charactersRepository
    val getCharactersUseCase = GetCharacters(charRepo, EmptyFilter())
    // create presenter. it will attach itself to the receiving view (this)
    CharactersPresenter(view = this, charactersUseCase = getCharactersUseCase,
        ioScheduler = Schedulers.io(), viewScheduler = AndroidSchedulers.mainThread())
  }

  override fun onResume() {
    super.onResume()
    presenter.start()
  }

  override fun onPause() {
    super.onPause()
    presenter.stop()
  }

  override fun setPresenter(presenter: CharactersContract.Presenter) {
    this.presenter = presenter
  }

  override fun showBigCenteredLoading() {
    mainCenteredProgressBar.visibility = View.VISIBLE
  }

  override fun showSmallBottomLoading() {
    smallBottomProgressBar.visibility = View.VISIBLE
    // TODO show/hide animation
  }

  override fun hideLoading() {
    mainCenteredProgressBar.visibility = View.GONE
    smallBottomProgressBar.visibility = View.GONE
  }

  override fun showCharacters(characters: List<Character>) {
    adapter.addCharacters(characters)
    Timber.d("showing characters")
  }

  override fun scrolledToBottomWithOffset(): Observable<Int> {
    return adapter.reachedBottomWithOffset().debounce(400, TimeUnit.MILLISECONDS)
  }

  override fun characterSelected(): Observable<Character> {
    return adapter.characterSelected().debounce(400, TimeUnit.MILLISECONDS)
  }

  override fun showCharacterDetails(characterId: Int) {
    navigator?.navigateToCharacterDetailsView(characterId)
    Timber.d("showing character details (id=$characterId)")
  }

  companion object {
    fun newInstance(): CharactersFragment = CharactersFragment()
  }
}

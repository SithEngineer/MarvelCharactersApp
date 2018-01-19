package io.github.sithengineer.marvelcharacters.characters

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.AutoCompleteTextView
import android.widget.ProgressBar
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import io.github.sithengineer.marvelcharacters.MarvelCharactersApplication
import io.github.sithengineer.marvelcharacters.R
import io.github.sithengineer.marvelcharacters.adapter.CharacterSearchCursorAdapter
import io.github.sithengineer.marvelcharacters.adapter.CharactersAdapter
import io.github.sithengineer.marvelcharacters.data.model.Character
import io.github.sithengineer.marvelcharacters.data.model.Image
import io.github.sithengineer.marvelcharacters.usecase.GetCharacters
import io.github.sithengineer.marvelcharacters.usecase.SearchCharacters
import io.github.sithengineer.marvelcharacters.usecase.filter.EmptyFilter
import io.github.sithengineer.marvelcharacters.usecase.filter.LimitFilter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import java.util.concurrent.TimeUnit


class CharactersFragment : Fragment(), CharactersContract.View {

  @BindView(R.id.fragment_characters_list)
  lateinit var characters: RecyclerView

  @BindView(R.id.fragment_characters_loading)
  lateinit var mainCenteredProgressBar: ProgressBar

  @BindView(R.id.fragment_characters_more_loading)
  lateinit var smallBottomProgressBar: ProgressBar

  private lateinit var searchView: SearchView
  private lateinit var searchPublisher: PublishSubject<String>
  private lateinit var searchItemIdPublisher: PublishSubject<Int>

  private lateinit var presenter: CharactersContract.Presenter
  private lateinit var viewUnBinder: Unbinder
  private lateinit var adapter: CharactersAdapter
  private lateinit var searchSuggestionsAdapter: CharacterSearchCursorAdapter
  private var navigator: CharactersNavigator? = null

  override fun onAttach(context: Context?) {
    super.onAttach(context)
    try {
      navigator = context as CharactersNavigator
    } catch (e: ClassCastException) {
      Timber.e(e, "Parent activity must implement ${CharactersNavigator::class.java.name}")
    }
  }

  override fun onDetach() {
    navigator = null
    super.onDetach()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    setHasOptionsMenu(true)
    return inflater.inflate(R.layout.fragment_characters, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewUnBinder = ButterKnife.bind(this, view)

    adapter = CharactersAdapter()
    searchSuggestionsAdapter = CharacterSearchCursorAdapter(
        context!!)
    characters.adapter = adapter
    characters.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    characters.layoutAnimation = AnimationUtils.loadLayoutAnimation(context,
        R.anim.list_animation_item_enter_from_bottom)

    searchPublisher = PublishSubject.create()
    searchItemIdPublisher = PublishSubject.create()
    attachPresenter()
  }

  override fun onDestroyView() {
    searchView.setOnSuggestionListener(null)
    searchView.setOnQueryTextListener(null)
    viewUnBinder.unbind()
    super.onDestroyView()
  }

  private fun attachPresenter() {
    val charactersRepository = (activity?.application as MarvelCharactersApplication).charactersRepository
    val getCharactersUseCase = GetCharacters(
        charactersRepository, EmptyFilter())
    val searchCharactersUseCase = SearchCharacters(
        charactersRepository,
        LimitFilter(SEARCH_RESULT_LIMIT))

    CharactersPresenter(view = this, getCharactersUseCase = getCharactersUseCase,
        searchCharactersUseCase = searchCharactersUseCase,
        ioScheduler = Schedulers.io(), viewScheduler = AndroidSchedulers.mainThread())
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    val layoutManager = characters.layoutManager
    outState.putParcelable(CHARACTERS_LIST_POSITION, layoutManager.onSaveInstanceState())
  }

  override fun onViewStateRestored(savedInstanceState: Bundle?) {
    super.onViewStateRestored(savedInstanceState)
    val layoutManager = characters.layoutManager
    layoutManager.onRestoreInstanceState(
        savedInstanceState?.getParcelable(CHARACTERS_LIST_POSITION))
  }

  override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
    super.onCreateOptionsMenu(menu, inflater)
    inflater?.inflate(R.menu.menu_search, menu)
    val item = menu?.findItem(R.id.menu_item_search)
    searchView = item?.actionView as SearchView
    searchView.suggestionsAdapter = searchSuggestionsAdapter
    searchView.layoutAnimation = AnimationUtils.loadLayoutAnimation(context,
        R.anim.list_animation_item_enter_from_top)

    // hack to show search suggestions with 1 character.
    val searchAutoCompleteTextView = searchView.findViewById(
        R.id.search_src_text) as AutoCompleteTextView
    searchAutoCompleteTextView.threshold = 1

    // RxSearchView bindings don't suffice
    searchView.setOnSuggestionListener(object : SearchView.OnSuggestionListener {
      override fun onSuggestionSelect(position: Int): Boolean = false

      override fun onSuggestionClick(position: Int): Boolean {
        searchItemIdPublisher.onNext(searchSuggestionsAdapter.getItemAt(position).id)
        return false
      }
    })

    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
      override fun onQueryTextSubmit(query: String?): Boolean = false

      override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null && newText.length >= MINIMUM_CHARS_TO_SEARCH) {
          searchPublisher.onNext(newText)
        } else {
          hideSearchResults()
        }
        return false
      }
    })
  }

  override fun onResume() {
    super.onResume()
    presenter.start()
  }

  override fun onPause() {
    presenter.stop()
    super.onPause()
  }

  override fun setPresenter(presenter: CharactersContract.Presenter) {
    this.presenter = presenter
  }

  override fun showBigCenteredLoading() {
    mainCenteredProgressBar.visibility = View.VISIBLE
    mainCenteredProgressBar.animate().setDuration(200L).alpha(1f)
  }

  override fun showSmallBottomLoading() {
    smallBottomProgressBar.visibility = View.VISIBLE
    smallBottomProgressBar.animate().setDuration(200L).translationYBy(-200f)
  }

  override fun hideLoading() {
    mainCenteredProgressBar.animate().setDuration(600L).alpha(0f).withEndAction({
      mainCenteredProgressBar.visibility = View.GONE
    })

    smallBottomProgressBar.animate().setDuration(600L).translationYBy(200f).withEndAction({
      smallBottomProgressBar.visibility = View.GONE
    })
  }

  override fun showCharacters(characters: List<Character>) {
    adapter.addCharacters(characters)
    Timber.d("Showing characters")
  }

  override fun onSearchedItemPressedWithId(): Observable<Int> {
    return searchItemIdPublisher
  }

  override fun onSearchedForTerm(): Observable<String> {
    return searchPublisher
  }

  private fun hideSearchResults() {
    Timber.d("Hiding search results")
    searchSuggestionsAdapter.setCharacters(emptyList())
  }

  override fun showSearchResult(characters: List<Character>) {
    Timber.d("Showing search results")
    searchSuggestionsAdapter.setCharacters(
        characters
            .filter { character -> character.name != null && character.id != null && character.thumbnail != null }
            .map { character ->
              CharacterSearchCursorAdapter.SearchCharacter(getImageUrl(character.thumbnail!!),
                  character.name!!,
                  character.id!!)
            })
  }

  private fun getImageUrl(image: Image): String = "${image.path}.${image.extension}"

  override fun onScrolledToBottomWithOffset(): Observable<Int> {
    return adapter.reachedBottomWithOffset().debounce(400, TimeUnit.MILLISECONDS)
  }

  override fun onCharacterSelected(): Observable<Character> {
    return adapter.characterSelected().debounce(400, TimeUnit.MILLISECONDS)
  }

  override fun showCharacterDetails(characterId: Int) {
    navigator?.navigateToCharacterDetailsView(characterId)
    Timber.d("showing character details (id=$characterId)")
  }

  companion object {
    private val CHARACTERS_LIST_POSITION = "charactersListPosition"
    private val SEARCH_RESULT_LIMIT = 5
    private val MINIMUM_CHARS_TO_SEARCH = 1

    fun newInstance(): CharactersFragment = CharactersFragment()
  }
}

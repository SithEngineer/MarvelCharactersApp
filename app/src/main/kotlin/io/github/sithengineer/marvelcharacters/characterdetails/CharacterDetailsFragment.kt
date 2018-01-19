package io.github.sithengineer.marvelcharacters.characterdetails

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ProgressBar
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding2.view.RxView
import io.github.sithengineer.marvelcharacters.MarvelCharactersApplication
import io.github.sithengineer.marvelcharacters.R
import io.github.sithengineer.marvelcharacters.adapter.SmallComicBookAdapter
import io.github.sithengineer.marvelcharacters.data.model.*
import io.github.sithengineer.marvelcharacters.usecase.GetSpecificCharacterDetails
import io.github.sithengineer.marvelcharacters.util.DisplayUtils
import io.github.sithengineer.marvelcharacters.util.MarvelImageView
import io.github.sithengineer.marvelcharacters.util.SpacingItemDecoration
import io.github.sithengineer.marvelcharacters.viewmodel.ComicBook
import io.github.sithengineer.marvelcharacters.viewmodel.ComicBookType
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit


class CharacterDetailsFragment : Fragment(), CharacterDetailsContract.View {

  @BindView(R.id.fragment_character_details_progress)
  lateinit var progressBar: ProgressBar

  @BindView(R.id.fragment_character_details_header_image)
  lateinit var characterImage: MarvelImageView

  @BindView(R.id.fragment_character_details_description_title)
  lateinit var descriptionTitle: TextView
  @BindView(R.id.fragment_character_details_description_text)
  lateinit var descriptionText: TextView

  @BindView(R.id.fragment_character_details_comics_title)
  lateinit var comicsTitle: TextView
  @BindView(R.id.fragment_character_details_comics_carousel)
  lateinit var comics: RecyclerView

  @BindView(R.id.fragment_character_details_series_title)
  lateinit var seriesTitle: TextView
  @BindView(R.id.fragment_character_details_series_carousel)
  lateinit var series: RecyclerView

  @BindView(R.id.fragment_character_details_stories_title)
  lateinit var storiesTitle: TextView
  @BindView(R.id.fragment_character_details_stories_carousel)
  lateinit var stories: RecyclerView

  @BindView(R.id.fragment_character_details_events_title)
  lateinit var eventsTitle: TextView
  @BindView(R.id.fragment_character_details_events_carousel)
  lateinit var events: RecyclerView

  @BindView(R.id.fragment_character_details_related_links_title)
  lateinit var relatedLinksTitle: TextView
  @BindView(R.id.fragment_character_details_related_links_detail)
  lateinit var relatedLinksDetail: TextView
  @BindView(R.id.fragment_character_details_related_links_wiki)
  lateinit var relatedLinksWiki: TextView
  @BindView(R.id.fragment_character_details_related_links_comic_link)
  lateinit var relatedLinksComicLink: TextView

  private lateinit var presenter: CharacterDetailsContract.Presenter
  private lateinit var comicsAdapter: SmallComicBookAdapter
  private lateinit var seriesAdapter: SmallComicBookAdapter
  private lateinit var storiesAdapter: SmallComicBookAdapter
  private lateinit var eventsAdapter: SmallComicBookAdapter
  private lateinit var viewUnBinder: Unbinder

  private var navigator: CharactersDetailsNavigator? = null

  override fun onAttach(context: Context?) {
    super.onAttach(context)
    try {
      navigator = context as CharactersDetailsNavigator
    } catch (e: ClassCastException) {
      Timber.e(e, "Parent activity must implement ${CharactersDetailsNavigator::class.java.name}")
    }
  }

  override fun onDetach() {
    navigator = null
    super.onDetach()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    setHasOptionsMenu(false)

    val actionBar = (activity as AppCompatActivity).supportActionBar
    actionBar?.setDisplayHomeAsUpEnabled(true)
    actionBar?.setDisplayShowHomeEnabled(true)

    return inflater.inflate(R.layout.fragment_character_details, container, false)
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {

    Timber.v("menu item clicked: $item")

    if (item?.itemId == android.R.id.home) {

      return true
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewUnBinder = ButterKnife.bind(this, view)

    val itemDecoration = SpacingItemDecoration(DisplayUtils.convertDpToPixel(context, 10),
        isVertical = false)
    val animation = AnimationUtils.loadLayoutAnimation(context,
        R.anim.list_animation_item_enter_from_right)

    comics.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    comicsAdapter = SmallComicBookAdapter()
    comics.adapter = comicsAdapter
    comics.addItemDecoration(itemDecoration)
    comics.layoutAnimation = animation

    series.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    seriesAdapter = SmallComicBookAdapter()
    series.adapter = seriesAdapter
    series.addItemDecoration(itemDecoration)
    series.layoutAnimation = animation

    stories.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    storiesAdapter = SmallComicBookAdapter()
    stories.adapter = storiesAdapter
    stories.addItemDecoration(itemDecoration)
    stories.layoutAnimation = animation

    events.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    eventsAdapter = SmallComicBookAdapter()
    events.adapter = eventsAdapter
    events.addItemDecoration(itemDecoration)
    events.layoutAnimation = animation

    attachPresenter(arguments?.getInt(CHARACTER_ID)!!)
  }

  override fun onDestroyView() {
    progressBar.animate().cancel()
    viewUnBinder.unbind()
    super.onDestroyView()
  }

  private fun attachPresenter(characterId: Int) {
    val charactersRepository = (activity?.application as MarvelCharactersApplication).charactersRepository
    val useCase = GetSpecificCharacterDetails(
        charactersRepository)
    CharacterDetailsPresenter(view = this, getCharacterDetailsUseCase = useCase,
        characterId = characterId,
        ioScheduler = Schedulers.io(), viewScheduler = AndroidSchedulers.mainThread())
  }

  override fun onResume() {
    super.onResume()
    presenter.start()
  }

  override fun onPause() {
    presenter.stop()
    super.onPause()
  }

  override fun setPresenter(presenter: CharacterDetailsContract.Presenter) {
    this.presenter = presenter
  }

  override fun showLoading() {
    progressBar.visibility = View.VISIBLE
    progressBar.animate().setDuration(200L).alpha(1f)
  }

  override fun hideLoading() {
    progressBar.visibility = View.GONE
    progressBar.animate().setDuration(600L).alpha(0f).withEndAction({
      progressBar.visibility = View.GONE
    })
  }

  override fun showCharacterDetails(character: Character) {
    character.thumbnail?.let {
      Glide.with(this)
          .load(getImageUrlFrom(it))
          .into(characterImage)
    }

    (activity as AppCompatActivity).supportActionBar?.title = character.name

    if (character.description != null && character.description.isNotBlank()) {
      descriptionText.text = character.description
    } else {
      descriptionTitle.visibility = View.GONE
      descriptionText.visibility = View.GONE
    }

    if (character.urls != null && character.urls.isNotEmpty()) {
      // use a list for this elements?
      relatedLinksTitle.visibility = View.VISIBLE
      character.urls.forEach {
        when (it.type?.toUpperCase()) {
          "DETAIL" -> {
            relatedLinksDetail.visibility = View.VISIBLE
            relatedLinksDetail.tag = it.url
          }
          "COMICLINK" -> {
            relatedLinksComicLink.visibility = View.VISIBLE
            relatedLinksComicLink.tag = it.url
          }
          "WIKI" -> {
            relatedLinksWiki.visibility = View.VISIBLE
            relatedLinksWiki.tag = it.url
          }
        }
      }
    }
  }

  override fun onSelectedRelatedLinksDetail(): Observable<String> {
    return RxView.clicks(relatedLinksDetail).map { _ -> relatedLinksDetail.tag as String }
  }

  override fun onSelectedRelatedLinksComicLink(): Observable<String> {
    return RxView.clicks(relatedLinksComicLink).map { _ -> relatedLinksComicLink.tag as String }
  }

  override fun onSelectedRelatedLinksWiki(): Observable<String> {
    return RxView.clicks(relatedLinksWiki).map { _ -> relatedLinksWiki.tag as String }
  }

  private fun getImageUrlFrom(image: Image?): String {
    return if (image != null) "${image.path}.${image.extension}" else ""
  }

  override fun showCharacterComics(comics: List<Comic>) {
    if (!comics.isEmpty()) {
      this.comicsTitle.visibility = View.VISIBLE
      this.comics.visibility = View.VISIBLE

      comicsAdapter.setComics(
          comics.map { comic ->
            ComicBook(
                getImageUrlFrom(comic.thumbnail),
                comic.title,
                ComicBookType.COMIC,
                comic.id
            )
          })
    }
  }

  override fun showCharacterEvents(events: List<Event>) {
    if (!events.isEmpty()) {
      this.eventsTitle.visibility = View.VISIBLE
      this.events.visibility = View.VISIBLE

      eventsAdapter.setComics(
          events.map { event ->
            ComicBook(
                getImageUrlFrom(event.thumbnail),
                event.title,
                ComicBookType.EVENT,
                event.id
            )
          })
    }
  }

  override fun showCharacterSeries(series: List<Series>) {
    if (!series.isEmpty()) {
      this.seriesTitle.visibility = View.VISIBLE
      this.series.visibility = View.VISIBLE

      seriesAdapter.setComics(
          series.map { seriesItem ->
            ComicBook(
                getImageUrlFrom(seriesItem.thumbnail),
                seriesItem.title,
                ComicBookType.SERIES,
                seriesItem.id
            )
          })
    }
  }

  override fun showCharacterStories(stories: List<Story>) {
    if (!stories.isEmpty()) {
      this.storiesTitle.visibility = View.VISIBLE
      this.stories.visibility = View.VISIBLE

      storiesAdapter.setComics(
          stories.map { story ->
            ComicBook(
                getImageUrlFrom(story.thumbnail),
                story.title,
                ComicBookType.STORY,
                story.id
            )
          })
    }
  }

  override fun onSelectedComicBook(): Observable<ComicBook> {
    return comicsAdapter.comicsSelected().debounce(400, TimeUnit.MILLISECONDS)
  }

  override fun onSelectedSeriesBook(): Observable<ComicBook> {
    return seriesAdapter.comicsSelected().debounce(400, TimeUnit.MILLISECONDS)
  }

  override fun onSelectedEventsBook(): Observable<ComicBook> {
    return eventsAdapter.comicsSelected().debounce(400, TimeUnit.MILLISECONDS)
  }

  override fun onSelectedStoriesBook(): Observable<ComicBook> {
    return storiesAdapter.comicsSelected().debounce(400, TimeUnit.MILLISECONDS)
  }

  override fun showComicsCovers(characterId: Int, comicBookType: ComicBookType) {
    navigator?.navigateToBookCovers(characterId, comicBookType)
  }

  override fun showUrl(url: String) {
    navigator?.showUrl(url)
  }

  companion object {

    private const val CHARACTER_ID = "characterId"

    fun newInstance(characterId: Int): CharacterDetailsFragment {
      val fragment = CharacterDetailsFragment()
      val args = Bundle()
      args.putInt(CHARACTER_ID, characterId)
      fragment.arguments = args
      return fragment
    }
  }
}
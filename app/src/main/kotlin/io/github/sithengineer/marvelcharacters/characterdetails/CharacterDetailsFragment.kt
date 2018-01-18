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
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding2.view.RxView
import io.github.sithengineer.marvelcharacters.MarvelCharactersApplication
import io.github.sithengineer.marvelcharacters.R
import io.github.sithengineer.marvelcharacters.characterdetails.adapter.CharacterDetailsComicBookAdapter
import io.github.sithengineer.marvelcharacters.characterdetails.usecase.GetSpecificCharacter
import io.github.sithengineer.marvelcharacters.data.model.*
import io.github.sithengineer.marvelcharacters.util.SpacingItemDecoration
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber


class CharacterDetailsFragment : Fragment(), CharacterDetailsContract.View {

  @BindView(R.id.fragment_character_details_progress)
  lateinit var progressBar: ProgressBar

  @BindView(R.id.fragment_character_details_header_image)
  lateinit var characterImage: ImageView

  @BindView(R.id.fragment_character_details_name)
  lateinit var characterName: TextView

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
  private lateinit var comicsAdapter: CharacterDetailsComicBookAdapter
  private lateinit var seriesAdapter: CharacterDetailsComicBookAdapter
  private lateinit var storiesAdapter: CharacterDetailsComicBookAdapter
  private lateinit var eventsAdapter: CharacterDetailsComicBookAdapter
  private lateinit var viewUnBinder: Unbinder

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

  private fun convertDpToPixel(context: Context?, dp: Int): Int {
    if (context != null) {
      val density = context.resources.displayMetrics.density
      return Math.round(dp.toFloat() * density)
    }
    return 0
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewUnBinder = ButterKnife.bind(this, view)

    val itemDecoration = SpacingItemDecoration(convertDpToPixel(context, 10), isVertical = false)

    comics.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    comicsAdapter = CharacterDetailsComicBookAdapter()
    comics.adapter = comicsAdapter
    comics.addItemDecoration(itemDecoration)

    series.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    seriesAdapter = CharacterDetailsComicBookAdapter()
    series.adapter = seriesAdapter
    series.addItemDecoration(itemDecoration)

    stories.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    storiesAdapter = CharacterDetailsComicBookAdapter()
    stories.adapter = storiesAdapter
    stories.addItemDecoration(itemDecoration)

    events.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    eventsAdapter = CharacterDetailsComicBookAdapter()
    events.adapter = eventsAdapter
    events.addItemDecoration(itemDecoration)

    attachPresenter(arguments?.getInt(CHARACTER_ID)!!)
  }

  override fun onDestroyView() {
    viewUnBinder.unbind()
    super.onDestroyView()
  }

  private fun attachPresenter(characterId: Int) {
    val charactersRepository = (activity?.application as MarvelCharactersApplication).charactersRepository
    val useCase = GetSpecificCharacter(charactersRepository)
    CharacterDetailsPresenter(view = this, getCharacterUseCase = useCase, characterId = characterId,
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
  }

  override fun hideLoading() {
    progressBar.visibility = View.GONE
  }

  override fun showCharacterDetails(character: Character) {
    character.thumbnail?.let {
      Glide.with(this)
          .load(getImageUrlFrom(it))
          .into(characterImage)
    }

    characterName.text = character.name

    if (character.description != null && character.description.isNotBlank()) {
      descriptionText.text = character.description
    } else {
      descriptionTitle.visibility = View.GONE
      descriptionText.visibility = View.GONE
    }

    // TODO ...

    Timber.v("Character urls: ${character.urls}")
    // relatedLinksTitle.visibility = View.VISIBLE
    //relatedLinksDetail.tag = character.urls!![0].type
    //relatedLinksComicLink.tag = character.urls!![0].type
    //relatedLinksWiki.tag = character.urls!![0].type
  }

  override fun selectedRelatedLinksDetail(): Observable<String> {
    return RxView.clicks(relatedLinksDetail).map { _ -> relatedLinksDetail.tag as String }
  }

  override fun selectedRelatedLinksComicLink(): Observable<String> {
    return RxView.clicks(relatedLinksComicLink).map { _ -> relatedLinksComicLink.tag as String }
  }

  override fun selectedRelatedLinksWiki(): Observable<String> {
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
            CharacterDetailsComicBookAdapter.ComicBook(
                getImageUrlFrom(comic.thumbnail),
                comic.title,
                CharacterDetailsComicBookAdapter.ComicBookType.COMIC,
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
            CharacterDetailsComicBookAdapter.ComicBook(
                getImageUrlFrom(event.thumbnail),
                event.title,
                CharacterDetailsComicBookAdapter.ComicBookType.EVENT,
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
            CharacterDetailsComicBookAdapter.ComicBook(
                getImageUrlFrom(seriesItem.thumbnail),
                seriesItem.title,
                CharacterDetailsComicBookAdapter.ComicBookType.SERIES,
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
            CharacterDetailsComicBookAdapter.ComicBook(
                getImageUrlFrom(story.thumbnail),
                story.title,
                CharacterDetailsComicBookAdapter.ComicBookType.STORY,
                story.id
            )
          })
    }
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
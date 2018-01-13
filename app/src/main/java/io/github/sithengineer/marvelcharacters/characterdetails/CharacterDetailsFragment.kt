package io.github.sithengineer.marvelcharacters.characterdetails

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import io.github.sithengineer.marvelcharacters.R
import io.github.sithengineer.marvelcharacters.characters.CharactersFragment

class CharacterDetailsFragment : Fragment() {

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

  @BindView(R.id.fragment_character_details_related_links_title)
  lateinit var relatedLinksTitle: TextView
  @BindView(R.id.fragment_character_details_related_links_detail)
  lateinit var relatedLinksDetail: TextView
  @BindView(R.id.fragment_character_details_related_links_wiki)
  lateinit var relatedLinksWiki: TextView
  @BindView(R.id.fragment_character_details_related_links_comic_link)
  lateinit var relatedLinksComicLink: TextView

  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    return inflater!!.inflate(
        R.layout.fragment_character_details, container, false)
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    // to do
  }

  companion object {
    fun newInstance(): CharactersFragment {
      val fragment = CharactersFragment()
      val args = Bundle()
      fragment.arguments = args
      return fragment
    }
  }
}
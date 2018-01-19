package io.github.sithengineer.marvelcharacters.characterdetails

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import butterknife.BindView
import butterknife.ButterKnife
import io.github.sithengineer.marvelcharacters.R
import io.github.sithengineer.marvelcharacters.comicbookcovers.ComicBookCoversActivity
import io.github.sithengineer.marvelcharacters.viewmodel.ComicBookType

class CharacterDetailsActivity : AppCompatActivity(), CharactersDetailsNavigator {

  @BindView(R.id.toolbar)
  lateinit var toolbar: Toolbar

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_character_details)
    ButterKnife.bind(this)
    setupToolbar()
    showFragment()
  }

  private fun showFragment() {
    supportFragmentManager
        .beginTransaction()
        .add(R.id.fragment_placeholder,
            CharacterDetailsFragment.newInstance(intent.extras.getInt(CHARACTER_ID)))
        .commit()
  }

  private fun setupToolbar() {
    toolbar.title = ""
    setSupportActionBar(toolbar)
    val ab = supportActionBar
    ab?.let {
      it.setDisplayHomeAsUpEnabled(true)
      it.setDisplayShowHomeEnabled(true)
    }
  }

  override fun navigateToBookCovers(characterId: Int, comicBookType: ComicBookType) {
    val intent = ComicBookCoversActivity.getIntent(this, characterId, comicBookType.name)
    startActivity(intent)
  }

  override fun showUrl(url: String) {
    val intent = Intent(Intent.ACTION_VIEW,  Uri.parse(url))
    startActivity(Intent.createChooser(intent, ""))
  }

  companion object {

    private const val CHARACTER_ID = "characterId"

    fun getIntent(context: Context, characterId: Int): Intent {
      val intent = Intent(context, CharacterDetailsActivity::class.java)
      val extras = Bundle()
      extras.putInt(CHARACTER_ID, characterId)
      intent.putExtras(extras)
      return intent
    }
  }
}
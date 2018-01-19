package io.github.sithengineer.marvelcharacters.comicbookcovers

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.github.sithengineer.marvelcharacters.R

class ComicBookCoversActivity : AppCompatActivity(), ComicBookCoversNavigator {

  override fun navigateBack() {
    onBackPressed()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_comic_book_covers)
    showFragment()
  }

  private fun showFragment() {
    supportFragmentManager
        .beginTransaction()
        .replace(R.id.fragment_placeholder,
            ComicBookCoversFragment.newInstance(
                intent.extras.getInt(CHARACTER_ID),
                intent.extras.getString(COMIC_BOOK_TYPE)
            )
        )
        .commit()
  }

  companion object {

    private const val CHARACTER_ID = "characterId"
    private const val COMIC_BOOK_TYPE = "comicBookType"

    fun getIntent(context: Context, characterId: Int, comicBookType: String): Intent {
      val intent = Intent(context, ComicBookCoversActivity::class.java)
      val extras = Bundle()
      extras.putInt(CHARACTER_ID, characterId)
      extras.putString(COMIC_BOOK_TYPE, comicBookType)
      intent.putExtras(extras)
      return intent
    }
  }
}
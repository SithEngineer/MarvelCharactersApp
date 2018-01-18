package io.github.sithengineer.marvelcharacters.characterdetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import butterknife.BindView
import butterknife.ButterKnife
import io.github.sithengineer.marvelcharacters.R

class CharacterDetailsActivity : AppCompatActivity() {

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
            CharacterDetailsFragment.newInstance(intent.extras.getInt(CHARACTER_ID)),
            "detail")
        .commit()
  }

  private fun setupToolbar() {
    setSupportActionBar(toolbar)
    val ab = supportActionBar
    ab?.let {
      it.setDisplayHomeAsUpEnabled(true)
      it.setDisplayShowHomeEnabled(true)
    }
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
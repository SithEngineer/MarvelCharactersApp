package io.github.sithengineer.marvelcharacters.characters

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import butterknife.BindView
import butterknife.ButterKnife
import io.github.sithengineer.marvelcharacters.AppNavigator
import io.github.sithengineer.marvelcharacters.R
import io.github.sithengineer.marvelcharacters.characterdetails.CharacterDetailsActivity

class CharactersActivity : AppCompatActivity(), AppNavigator {

  @BindView(R.id.toolbar)
  lateinit var toolbar: Toolbar

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_characters)
    ButterKnife.bind(this)
    setupToolbar()
    showFragment()
  }

  override fun navigateToCharacterDetailsView(characterId: Int) {
    val intent = CharacterDetailsActivity.getIntent(this, characterId)
    startActivity(intent)
  }

  private fun showFragment() {
    supportFragmentManager
        .beginTransaction()
        .add(R.id.fragment_placeholder, CharactersFragment.newInstance())
        .commit()
  }


  private fun setupToolbar() {
    setSupportActionBar(toolbar)
    val ab = supportActionBar
    ab?.let {
      it.setDisplayHomeAsUpEnabled(false)
      it.setDisplayShowHomeEnabled(false)
    }
  }
}

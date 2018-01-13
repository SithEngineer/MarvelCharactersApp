package io.github.sithengineer.marvelcharacters

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import butterknife.ButterKnife
import io.github.sithengineer.marvelcharacters.characters.CharactersFragment

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    ButterKnife.bind(this)

    supportFragmentManager
        .beginTransaction()
        .add(R.id.fragment_placeholder, CharactersFragment.newInstance())
        .commit()
  }
}

package io.github.sithengineer.marvelcharacters

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import butterknife.ButterKnife
import io.github.sithengineer.marvelcharacters.characterdetails.CharacterDetailsFragment
import io.github.sithengineer.marvelcharacters.characters.CharactersFragment

class MainActivity : AppCompatActivity(), AppNavigator {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    ButterKnife.bind(this)
    navigateToCharactersView()
  }

  override fun navigateToCharacterDetailsView(characterId: Int){
    supportFragmentManager
        .beginTransaction()
        .add(R.id.fragment_placeholder, CharacterDetailsFragment.newInstance(characterId))
        .commit()
  }

  override fun navigateToCharactersView(){
    supportFragmentManager
        .beginTransaction()
        .add(R.id.fragment_placeholder, CharactersFragment.newInstance())
        .commit()
  }
}

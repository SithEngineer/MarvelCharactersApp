package io.github.sithengineer.marvelcharacters

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.widget.FrameLayout
import butterknife.BindView
import butterknife.ButterKnife

class HomeActivity : AppCompatActivity() {

  @BindView(R.id.navigation)
  lateinit var bottomNavigation: BottomNavigationView

  @BindView(R.id.fragment_placeholder)
  lateinit var fragmentPlaceholder: FrameLayout

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_home)
    ButterKnife.bind(this)

    supportFragmentManager
        .beginTransaction()
        .add(R.id.fragment_placeholder, CharactersFragment.newInstance())
        .commit()
  }
}

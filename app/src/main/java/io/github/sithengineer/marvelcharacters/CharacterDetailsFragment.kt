package io.github.sithengineer.marvelcharacters

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by sithengineer on 13-01-2018.
 */
class CharacterDetailsFragment : Fragment() {

  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    return inflater!!.inflate(R.layout.fragment_character_details, container, false)
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
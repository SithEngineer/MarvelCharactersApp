package io.github.sithengineer.marvelcharacters.characters

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import io.github.sithengineer.marvelcharacters.R

class CharactersFragment : Fragment() {

  @BindView(R.id.fragment_characters_list)
  lateinit var characters: RecyclerView

  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    return inflater!!.inflate(R.layout.fragment_characters, container, false)
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

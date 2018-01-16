package io.github.sithengineer.marvelcharacters.characters

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import io.github.sithengineer.marvelcharacters.MarvelCharactersApplication
import io.github.sithengineer.marvelcharacters.R
import io.github.sithengineer.marvelcharacters.characters.filter.EmptyFilter
import io.github.sithengineer.marvelcharacters.characters.usecase.GetCharacters
import io.github.sithengineer.marvelcharacters.data.model.Character
import io.reactivex.Flowable

class CharactersFragment : Fragment(), CharactersContract.View {
  override fun setPresenter(presenter: CharactersContract.Presenter) {
    TODO("not implemented")
  }

  override fun showLoading() {
    TODO("not implemented")
  }

  override fun hideLoading() {
    TODO("not implemented")
  }

  override fun showCharacters(characters: List<Character>) {
    TODO("not implemented")
  }

  override fun characterSelected(): Flowable<Character> {
    TODO("not implemented")
  }

  override fun showCharacterDetails(characterId: Int) {
    TODO("not implemented")
  }

  @BindView(R.id.fragment_characters_list)
  lateinit var characters: RecyclerView

  private lateinit var presenter: CharactersPresenter

  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    return inflater!!.inflate(R.layout.fragment_characters, container, false)
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    presenter = getPresenter()
  }

  private fun getPresenter(): CharactersPresenter {
    val charRepo = (activity.application as MarvelCharactersApplication).charactersRepository
    val getCharactersUseCase = GetCharacters(charRepo, EmptyFilter())
    return CharactersPresenter(this, getCharactersUseCase)
  }

  override fun onResume() {
    super.onResume()
    presenter.start()
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

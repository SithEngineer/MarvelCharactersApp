package io.github.sithengineer.marvelcharacters.characters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.jakewharton.rxbinding2.view.RxView
import io.github.sithengineer.marvelcharacters.R
import io.github.sithengineer.marvelcharacters.data.model.Character
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class CharactersAdapter : RecyclerView.Adapter<CharactersAdapter.ViewHolder>() {

  private var charactersShowing: Set<Character> = hashSetOf()
  private val bottomPublisher: PublishSubject<Int> = PublishSubject.create()
  private val characterSelectedPublisher: PublishSubject<Character> = PublishSubject.create()

  private companion object {
    val BOTTOM_OFFSET = 1
  }

  fun addCharacters(characters: List<Character>) {
    charactersShowing += characters
    notifyDataSetChanged()
  }

  override fun getItemCount(): Int = charactersShowing.size

  override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
    holder?.bind(charactersShowing.elementAt(position))
    if (position >= (charactersShowing.size - BOTTOM_OFFSET)) {
      bottomPublisher.onNext(position)
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent?.context).inflate(ViewHolder.LAYOUT_ID, parent, false)
    return ViewHolder(view, characterSelectedPublisher)
  }

  fun reachedBottomWithOffset(): Observable<Int> = bottomPublisher
  fun characterSelected(): Observable<Character> = characterSelectedPublisher

  class ViewHolder(itemView: View?,
      private val characterSelectedPublisher: PublishSubject<Character>) : RecyclerView.ViewHolder(
      itemView) {

    @BindView(R.id.list_item_character_image)
    lateinit var image: ImageView

    @BindView(R.id.list_item_character_name)
    lateinit var name: TextView

    private lateinit var character: Character

    internal companion object {
      val LAYOUT_ID = R.layout.list_item_character
    }

    init {
      itemView?.let {
        ButterKnife.bind(this, it)
        // TODO find a way to unsubscribe properly
        RxView.clicks(it).subscribe { _ -> characterSelectedPublisher.onNext(character) }
      }
    }

    fun bind(character: Character) {
      this.character = character
      name.text = character.name

      val thumbnail = character.thumbnail
      val imageUrl = "${thumbnail?.path}.${thumbnail?.extension}"
      val glideRequestManager = Glide.with(itemView)

      if (character.thumbnail?.extension.equals("gif", true))
        glideRequestManager.asGif()

      // TODO improve error handling in image loading
      val requestOptions = RequestOptions()
          .placeholder(R.color.background)
          .error(R.color.background)

      glideRequestManager
          .load(imageUrl)
          .apply(requestOptions)
          .into(image)
    }
  }
}
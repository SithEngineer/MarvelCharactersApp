package io.github.sithengineer.marvelcharacters.presentation.adapter

import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.request.ImageRequest
import com.jakewharton.rxbinding2.view.RxView
import io.github.sithengineer.marvelcharacters.R
import io.github.sithengineer.marvelcharacters.data.source.apimodel.Character
import io.github.sithengineer.marvelcharacters.presentation.adapter.CharactersAdapter.ViewHolder
import io.github.sithengineer.marvelcharacters.presentation.adapter.CharactersAdapter.ViewHolder.Companion
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class CharactersAdapter : RecyclerView.Adapter<ViewHolder>() {

  private var charactersShowing: Set<Character> = hashSetOf()
  private val characterSelectedPublisher: PublishSubject<Character> = PublishSubject.create()
  private val bottomPublisher: PublishSubject<Int> = PublishSubject.create()

  private companion object {
    const val BOTTOM_OFFSET = 1
  }

  fun addCharacters(characters: List<Character>) {
    charactersShowing += characters
    notifyDataSetChanged()
  }

  override fun getItemCount(): Int = charactersShowing.size

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bind(charactersShowing.elementAt(position))
    if (position >= (charactersShowing.size - BOTTOM_OFFSET)) {
      bottomPublisher.onNext(position)
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(
        ViewHolder.LAYOUT_ID, parent, false)
    return ViewHolder(
        view, characterSelectedPublisher)
  }

  fun reachedBottomWithOffset(): Observable<Int> = bottomPublisher

  fun characterSelected(): Observable<Character> = characterSelectedPublisher

  class ViewHolder(itemView: View?,
      private val characterSelectedPublisher: PublishSubject<Character>) : RecyclerView.ViewHolder(
      itemView) {

    @BindView(R.id.list_item_character_image)
    lateinit var image: SimpleDraweeView

    @BindView(R.id.list_item_character_name)
    lateinit var name: TextView

    private lateinit var character: Character

    internal companion object {
      const val LAYOUT_ID = R.layout.list_item_character
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

      // TODO improve error handling in image loading
      image.controller = Fresco
          .newDraweeControllerBuilder()
          .setOldController(image.controller)
          .setImageRequest(ImageRequest.fromUri(Uri.parse(imageUrl)))
          .setRetainImageOnFailure(true)
          .build()
    }
  }
}
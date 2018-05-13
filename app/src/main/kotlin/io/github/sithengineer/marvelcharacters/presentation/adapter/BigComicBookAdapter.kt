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
import io.github.sithengineer.marvelcharacters.R
import io.github.sithengineer.marvelcharacters.presentation.adapter.BigComicBookAdapter.ViewHolder
import io.github.sithengineer.marvelcharacters.presentation.adapter.BigComicBookAdapter.ViewHolder.Companion
import io.github.sithengineer.marvelcharacters.presentation.viewmodel.ComicBook

class BigComicBookAdapter : RecyclerView.Adapter<ViewHolder>() {

  private var comicBooks: List<ComicBook> = emptyList()

  fun setComics(comicBooks: List<ComicBook>) {
    this.comicBooks = comicBooks
    notifyDataSetChanged()
  }

  override fun getItemCount(): Int = comicBooks.size

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bind(comicBooks.elementAt(position), position, comicBooks.size)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(
        ViewHolder.LAYOUT_ID, parent, false)
    return ViewHolder(
        view)
  }

  class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

    @BindView(R.id.list_item_book_image)
    lateinit var image: SimpleDraweeView

    @BindView(R.id.list_item_book_text)
    lateinit var description: TextView

    @BindView(R.id.list_item_book_count)
    lateinit var count: TextView

    private lateinit var comicBook: ComicBook

    internal companion object {
      const val LAYOUT_ID = R.layout.list_item_comic_book_big
    }

    init {
      itemView?.let {
        ButterKnife.bind(this, it)
      }
    }

    fun bind(comicBook: ComicBook,
        position: Int, total: Int) {
      this.comicBook = comicBook
      description.text = comicBook.name
      val countText = "${position + 1} / $total"
      count.text = countText
      image.controller = Fresco.newDraweeControllerBuilder()
          .setOldController(image.controller)
          .setImageRequest(ImageRequest.fromUri(
              Uri.parse(comicBook.imageUrl)))
          .build()
    }
  }
}
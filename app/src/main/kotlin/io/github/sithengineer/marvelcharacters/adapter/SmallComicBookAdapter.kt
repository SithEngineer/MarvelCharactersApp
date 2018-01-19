package io.github.sithengineer.marvelcharacters.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding2.view.RxView
import io.github.sithengineer.marvelcharacters.R
import io.github.sithengineer.marvelcharacters.viewmodel.ComicBook
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class SmallComicBookAdapter : RecyclerView.Adapter<SmallComicBookAdapter.ViewHolder>() {

  private var comicBooks: List<ComicBook> = emptyList()
  private val comicsSelectedPublisher: PublishSubject<ComicBook> = PublishSubject.create()

  fun setComics(comicBooks: List<ComicBook>) {
    this.comicBooks = comicBooks
    notifyDataSetChanged()
  }

  fun comicsSelected(): Observable<ComicBook> = comicsSelectedPublisher

  override fun getItemCount(): Int = comicBooks.size

  override fun onBindViewHolder(holder: ViewHolder?,
      position: Int) {
    holder?.bind(comicBooks.elementAt(position))
  }

  override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent?.context).inflate(ViewHolder.LAYOUT_ID, parent, false)
    return ViewHolder(comicsSelectedPublisher, view)
  }

  class ViewHolder(private val comicSummarySelectedPublisher: PublishSubject<ComicBook>,
      itemView: View?) : RecyclerView.ViewHolder(itemView) {

    @BindView(R.id.list_item_book_image)
    lateinit var image: ImageView

    @BindView(R.id.list_item_book_text)
    lateinit var description: TextView

    private lateinit var comicBook: ComicBook

    internal companion object {
      const val LAYOUT_ID = R.layout.list_item_comic_book_small
    }

    init {
      itemView?.let {
        ButterKnife.bind(this, it)
        RxView.clicks(it).subscribe { _ -> comicSummarySelectedPublisher.onNext(comicBook) }
      }
    }

    fun bind(comicBook: ComicBook) {
      this.comicBook = comicBook
      description.text = comicBook.name
      Glide.with(itemView).load(comicBook.imageUrl).into(image)
    }
  }
}
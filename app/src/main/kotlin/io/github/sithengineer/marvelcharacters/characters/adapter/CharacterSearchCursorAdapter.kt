package io.github.sithengineer.marvelcharacters.characters.adapter

import android.content.Context
import android.database.Cursor
import android.database.MatrixCursor
import android.support.v4.widget.CursorAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import io.github.sithengineer.marvelcharacters.R

class CharacterSearchCursorAdapter(context: Context) : CursorAdapter(context, null,
    false) {

  private companion object {
    private val ITEM_LAYOUT = R.layout.list_item_search_result
    private val COLUMN_ID = "_id"
    private val COLUMN_IMAGE_URL = "image_url"
    private val COLUMN_NAME = android.app.SearchManager.SUGGEST_COLUMN_TEXT_1
    private val COLUMN_NAMES = arrayOf(COLUMN_ID, COLUMN_NAME, COLUMN_IMAGE_URL)
  }

  private var characters: List<SearchCharacter> = emptyList()
  fun setCharacters(characters: List<SearchCharacter>) {
    this.characters = characters
    if (characters.isEmpty()) {
      changeCursor(null)
    } else {
      changeCursor(getCursorFor(characters))
    }
  }

  private fun getCursorFor(characters: List<SearchCharacter>): Cursor {
    val cursor = MatrixCursor(COLUMN_NAMES, characters.size)
    for (item in characters) {
      cursor.newRow()
          .add(item.id)
          .add(item.name)
          .add(item.imageUrl)
    }
    return cursor
  }

  override fun newView(context: Context?, cursor: Cursor?, parent: ViewGroup?): View {
    return LayoutInflater.from(context).inflate(ITEM_LAYOUT, parent, false)
  }

  override fun bindView(view: View?, context: Context?, cursor: Cursor?) {
    if (cursor != null && view != null) {
      val character = getSuggestionAtCurrentPosition(cursor)
      val imageView = view.findViewById<ImageView>(R.id.list_item_search_result_character_image)
      context?.let { Glide.with(it).load(character.imageUrl).into(imageView) }
      view.findViewById<TextView>(R.id.list_item_search_result_character_name).text = character.name
    }
  }

  fun getItemAt(position: Int): SearchCharacter {
    val cursor = cursor
    if (cursor.moveToPosition(position)) {
      return getSuggestionAtCurrentPosition(cursor)
    }
    throw UnsupportedOperationException(
        "Unable to get ${SearchCharacter::class.java.name} at position $position")
  }

  private fun getSuggestionAtCurrentPosition(cursor: Cursor): SearchCharacter {
    return SearchCharacter(
        imageUrl = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_URL)),
        name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
        id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
    )
  }

  data class SearchCharacter(val imageUrl: String, val name: String, val id: Int)
}
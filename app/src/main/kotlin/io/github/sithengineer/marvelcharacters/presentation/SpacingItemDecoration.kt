package io.github.sithengineer.marvelcharacters.presentation

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View


class SpacingItemDecoration(
    private val spaceInPx: Int,
    private val isVertical: Boolean = true
) : RecyclerView.ItemDecoration() {

  override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
      state: RecyclerView.State) {
    val position = parent.getChildViewHolder(view).adapterPosition
    val itemCount = state.itemCount

    if (isVertical) {
      outRect.left = spaceInPx
      outRect.right = spaceInPx
      outRect.top = spaceInPx
      outRect.bottom = if (position == itemCount - 1) spaceInPx else 0
    } else {
      // apply horizontal spacing
      outRect.left = spaceInPx
      outRect.right = if (position == itemCount - 1) spaceInPx else 0
      outRect.top = spaceInPx
      outRect.bottom = spaceInPx
    }
  }
}
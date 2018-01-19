package io.github.sithengineer.marvelcharacters.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.os.Build
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import io.github.sithengineer.marvelcharacters.R

class MarvelImageView(context: Context?, attrs: AttributeSet?,
    defStyleAttr: Int) : AppCompatImageView(context, attrs, defStyleAttr) {

  constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

  constructor(context: Context) : this(context, null, 0)

  private val pathToClip by lazy { Path() }
  private val characterMainColorPath by lazy { Path() }
  private val characterMainColorPaint by lazy {
    Paint().apply {
      style = Paint.Style.STROKE
      strokeWidth = DisplayUtils.convertDpToPixel(context, 5).toFloat()
      color = getAccentColor()
      isAntiAlias = true
    }
  }

  private fun getAccentColor(): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      resources.getColor(R.color.colorAccent, context?.theme)
    } else {
      resources.getColor(R.color.colorAccent)
    }
  }

  override fun onDraw(canvas: Canvas?) {
    if (canvas != null) {
      val maxWidth = resources.displayMetrics.widthPixels
      val maxHeight = height
      val leftHeightOffsetWithoutCharacterColorLine = DisplayUtils.convertDpToPixel(context, 40)
      val rightHeightOffsetWithoutCharacterColorLine = DisplayUtils.convertDpToPixel(context, 10)
      val leftHeightOffsetCharacterColorLine = DisplayUtils.convertDpToPixel(context, 40)
      val rightHeightOffsetCharacterColorLine = DisplayUtils.convertDpToPixel(context, 10)

      with(characterMainColorPath) {
        characterMainColorPath.setLastPoint(0f,
            (maxHeight - leftHeightOffsetCharacterColorLine).toFloat())
        characterMainColorPath.lineTo(maxWidth.toFloat(),
            (maxHeight - rightHeightOffsetCharacterColorLine).toFloat())
      }

      canvas.drawPath(characterMainColorPath, characterMainColorPaint)

      with(pathToClip) {
        pathToClip.lineTo(0f, (maxHeight - leftHeightOffsetWithoutCharacterColorLine).toFloat())
        pathToClip.lineTo(maxWidth.toFloat(),
            (maxHeight - rightHeightOffsetWithoutCharacterColorLine).toFloat())
        pathToClip.lineTo(maxWidth.toFloat(), 0f)
        pathToClip.lineTo(0f, 0f)
      }

      canvas.clipPath(pathToClip)

      canvas.save()
    }

    super.onDraw(canvas)
  }
}
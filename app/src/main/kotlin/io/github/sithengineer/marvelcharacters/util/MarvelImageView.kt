package io.github.sithengineer.marvelcharacters.util

import android.content.Context
import android.util.AttributeSet
import com.github.siyamed.shapeimageview.ShaderImageView
import com.github.siyamed.shapeimageview.shader.ShaderHelper
import com.github.siyamed.shapeimageview.shader.SvgShader
import io.github.sithengineer.marvelcharacters.R

class MarvelImageView(context: Context, attrs: AttributeSet?, defStyle: Int) : ShaderImageView(
    context, attrs, defStyle) {

  constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

  constructor(context: Context) : this(context, null, 0)

  override fun createImageViewHelper(): ShaderHelper {
    return SvgShader(R.raw.imgview_marvel_mk3)
  }
}
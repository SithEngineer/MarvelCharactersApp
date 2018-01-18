package io.github.sithengineer.marvelcharacters.characters.usecase.filter

import io.github.sithengineer.marvelcharacters.data.model.Character
import timber.log.Timber

class LimitFilter(private val limit: Int) : CharacterFilter {
  override fun filter(characters: List<Character>): List<Character> {
    Timber.v("limiting first $limit results")
    return characters.take(limit)
  }
}
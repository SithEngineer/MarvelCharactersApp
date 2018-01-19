package io.github.sithengineer.marvelcharacters.usecase.filter

import io.github.sithengineer.marvelcharacters.data.model.Character

class EmptyFilter : CharacterFilter {
  override fun filter(characters: List<Character>): List<Character> {
    return characters
  }
}
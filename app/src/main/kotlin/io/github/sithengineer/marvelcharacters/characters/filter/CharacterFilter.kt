package io.github.sithengineer.marvelcharacters.characters.filter

import io.github.sithengineer.marvelcharacters.data.model.Character

interface CharacterFilter {
  fun filter(characters: List<Character>) : List<Character>
}
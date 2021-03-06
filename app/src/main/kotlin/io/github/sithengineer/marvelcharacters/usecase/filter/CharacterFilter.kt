package io.github.sithengineer.marvelcharacters.usecase.filter

import io.github.sithengineer.marvelcharacters.data.source.apimodel.Character

interface CharacterFilter {
  fun filter(characters: List<Character>) : List<Character>
}
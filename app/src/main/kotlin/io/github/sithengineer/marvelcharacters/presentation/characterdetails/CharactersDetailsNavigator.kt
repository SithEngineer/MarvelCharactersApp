package io.github.sithengineer.marvelcharacters.presentation.characterdetails

import io.github.sithengineer.marvelcharacters.presentation.viewmodel.ComicBookType

interface CharactersDetailsNavigator {
  fun navigateToBookCovers(characterId: Int, comicBookType: ComicBookType)
  fun showUrl(url: String)
}
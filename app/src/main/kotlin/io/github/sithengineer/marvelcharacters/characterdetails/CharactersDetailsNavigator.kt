package io.github.sithengineer.marvelcharacters.characterdetails

import io.github.sithengineer.marvelcharacters.viewmodel.ComicBookType

interface CharactersDetailsNavigator {
  fun navigateToBookCovers(characterId: Int, comicBookType: ComicBookType)
  fun showUrl(url: String)
}
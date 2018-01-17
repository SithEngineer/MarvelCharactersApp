package io.github.sithengineer.marvelcharacters

interface AppNavigator {
  fun navigateToCharacterDetailsView(characterId: Int)
  fun navigateToCharactersView()
}
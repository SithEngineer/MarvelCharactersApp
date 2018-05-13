package io.github.sithengineer.marvelcharacters.presentation

interface BaseView<T : BasePresenter> {
  fun setPresenter(presenter: T)
}
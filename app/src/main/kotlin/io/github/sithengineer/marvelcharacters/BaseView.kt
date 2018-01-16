package io.github.sithengineer.marvelcharacters

interface BaseView<T : BasePresenter> {
  fun setPresenter(presenter: T)
}
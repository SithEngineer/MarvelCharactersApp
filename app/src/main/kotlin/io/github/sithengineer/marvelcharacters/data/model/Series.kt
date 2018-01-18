package io.github.sithengineer.marvelcharacters.data.model

data class Series(
    val id: Int,
    val title: String,
    val description: String,
    val thumbnail: Image?
)
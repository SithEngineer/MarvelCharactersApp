package io.github.sithengineer.marvelcharacters.data.source.apimodel

data class Series(
    val id: Int,
    val title: String,
    val description: String,
    val thumbnail: Image?
)
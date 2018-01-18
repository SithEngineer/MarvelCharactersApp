package io.github.sithengineer.marvelcharacters.data.model

data class Event (
    val id: Int,
    val title: String,
    val description: String,
    val thumbnail: Image?
)
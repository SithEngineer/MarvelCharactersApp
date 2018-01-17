package io.github.sithengineer.marvelcharacters.data.model

data class MarvelList<out T>(
    val available: Int?,
    val returned: Int?,
    val collectionURI: String?,
    val items: List<T>?
)
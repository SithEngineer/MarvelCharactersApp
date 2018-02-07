package io.github.sithengineer.marvelcharacters.data.source.apimodel

data class MarvelList<out T>(
    val available: Int?,
    val returned: Int?,
    val collectionURI: String?,
    val items: List<T>?
)
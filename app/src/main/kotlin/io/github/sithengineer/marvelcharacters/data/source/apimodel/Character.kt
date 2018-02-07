package io.github.sithengineer.marvelcharacters.data.source.apimodel

import java.util.*

data class Character(
    val id: Int?,
    val name: String?,
    val description: String?,
    val modified: Date?,
    val thumbnail: Image?,
    val resourceURI: String?,
    val comics: MarvelList<ComicSummary>?,
    val urls: List<Url>?,
    val stories: MarvelList<StorySummary>?,
    val events: MarvelList<EventSummary>?,
    val series: MarvelList<SeriesSummary>?
)
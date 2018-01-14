package io.github.sithengineer.marvelcharacters.data.model

data class Character(
    val id: Int,
    val name: String,
    val description: String,
    val modified: String, // date
    val resourceURI: String,
    val urls: List<Url>,
    val thumbnail: Image,
    val comics: MarvelList<ComicSummary>,
    val stories: MarvelList<StorySummary>,
    val events: MarvelList<EventSummary>,
    val series: MarvelList<SeriesSummary>
)
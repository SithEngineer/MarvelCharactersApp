package io.github.sithengineer.marvelcharacters.data.model

data class CharacterDataWrapper(
    val code: Int?,
    val status: String?,
    val copyright: String?,
    val attributionText: String?,
    val attributionHTML: String?,
    val etag: String?,
    val data: CharacterDataContainer?
)
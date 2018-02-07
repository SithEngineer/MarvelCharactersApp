package io.github.sithengineer.marvelcharacters.data.source.apimodel

data class DataWrapper<T>(
    val code: Int?,
    val status: String?,
    val copyright: String?,
    val attributionText: String?,
    val attributionHTML: String?,
    val etag: String?,
    val data: DataContainer<T>
)
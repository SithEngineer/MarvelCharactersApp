package io.github.sithengineer.marvelcharacters.data.source.apimodel

data class DataContainer<T>(
    val offset: Int?,
    val limit: Int?,
    val total: Int?,
    val count: Int?,
    val results: List<T>?
)
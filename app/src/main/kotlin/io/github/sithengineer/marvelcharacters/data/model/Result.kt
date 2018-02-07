package io.github.sithengineer.marvelcharacters.data.model

data class Result<out T>(
    val data: T,
    val errorFriendly: String,
    val error: Exception
)
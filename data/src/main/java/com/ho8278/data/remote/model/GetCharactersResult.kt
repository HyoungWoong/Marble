package com.ho8278.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class GetCharactersResult(
    val code: Int = 0,
    val data: CharacterDataContainer?,
)

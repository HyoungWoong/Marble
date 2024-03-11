package com.ho8278.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class CharacterDataContainer(
    val offset: Int = 0,
    val limit: Int = 0,
    val total: Int = 0,
    val count: Int = 0,
    val results: List<Character>?,
)
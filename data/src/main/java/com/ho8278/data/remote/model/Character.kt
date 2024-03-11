package com.ho8278.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class Character(
    val id: Int?,
    val name: String?,
    val description: String?,
    val thumbnail: Image?,
)
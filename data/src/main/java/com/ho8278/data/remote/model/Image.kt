package com.ho8278.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class Image(
    val path: String?,
    val extension: String?,
)
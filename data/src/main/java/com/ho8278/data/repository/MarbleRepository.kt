package com.ho8278.data.repository

import com.ho8278.data.model.SearchResult
import kotlinx.coroutines.flow.Flow

interface MarbleRepository {
    suspend fun search(nameStartsWith: String, offset: Int): SearchResult
    suspend fun getFavorites(): List<String>
    suspend fun setFavorite(id: String)
    suspend fun removeFavorite(id: String)
    fun favoriteChanges(): Flow<List<String>>
}
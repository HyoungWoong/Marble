package com.ho8278.data.repository

import com.ho8278.data.model.SearchResult
import kotlinx.coroutines.flow.Flow

interface MarbleRepository {
    suspend fun search(nameStartsWith: String, offset: Int): SearchResult
    suspend fun getFavorites(): List<Int>
    suspend fun setFavorite(id: Int)
    suspend fun removeFavorite(id: Int)
    fun favoriteChanges(): Flow<List<Int>>
}
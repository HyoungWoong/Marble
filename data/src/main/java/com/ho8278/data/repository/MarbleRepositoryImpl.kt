package com.ho8278.data.repository

import com.ho8278.data.BuildConfig
import com.ho8278.data.local.FavoritePref
import com.ho8278.data.model.Card
import com.ho8278.data.model.Favorites
import com.ho8278.data.model.SearchResult
import com.ho8278.data.remote.model.toUrl
import com.ho8278.data.remote.service.MarbleService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.onStart
import java.security.MessageDigest

class MarbleRepositoryImpl(
    private val marbleService: MarbleService,
    private val favoritePref: FavoritePref,
) : MarbleRepository {

    private val favoriteChangesEvent = MutableSharedFlow<List<String>>()

    override suspend fun search(nameStartsWith: String, offset: Int): SearchResult {
        val timestamp = System.currentTimeMillis()
        val hash = getHash(timestamp)
        val characterResult = marbleService.getCharacters(nameStartsWith, hash, timestamp, offset)
        val characters = characterResult.data?.results.orEmpty()
            .filter { it.id != null }
            .map {
                Card(
                    it.id!!,
                    it.name.orEmpty(),
                    it.description.orEmpty(),
                    it.thumbnail?.toUrl().orEmpty()
                )
            }

        return SearchResult(characterResult.data?.total ?: 0, characters)
    }

    override suspend fun getFavorites(): List<String> {
        val favorites = favoritePref.getValue<Favorites>(KEY_FAVORITE, Favorites::class.java)
        return favorites?.ids.orEmpty()
    }

    override suspend fun setFavorite(id: String) {
        val favoriteIds = (getFavorites() + id).toSet()
        val newFavorites = Favorites(favoriteIds.toList())

        favoritePref.putValue(KEY_FAVORITE, newFavorites, Favorites::class.java)
        favoriteChangesEvent.emit(newFavorites.ids)
    }

    override suspend fun removeFavorite(id: String) {
        val favoriteIds = getFavorites() - id
        val newFavorites = Favorites(favoriteIds)

        favoritePref.putValue(KEY_FAVORITE, newFavorites, Favorites::class.java)
        favoriteChangesEvent.emit(newFavorites.ids)
    }

    override fun favoriteChanges(): Flow<List<String>> {
        return favoriteChangesEvent.onStart { emit(getFavorites()) }
    }

    private fun getHash(timestamp: Long): String {
        val digestInput = "$timestamp${BuildConfig.PRIVATE_KEY}${BuildConfig.API_KEY}"
        return MessageDigest.getInstance("MD5").digest(digestInput.toByteArray()).toHexString()
    }

    private fun ByteArray.toHexString() = joinToString("") { "%02x".format(it) }

    companion object {
        private const val KEY_FAVORITE = "key_favorite"
    }
}
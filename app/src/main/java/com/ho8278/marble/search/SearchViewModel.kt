package com.ho8278.marble.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ho8278.core.error.stable
import com.ho8278.data.model.SearchResult
import com.ho8278.data.repository.MarbleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val marbleRepository: MarbleRepository
) : ViewModel() {

    private var isInitialize = false

    private val searchResult = MutableStateFlow<SearchResult?>(null)
    val searchText = MutableStateFlow("")

    val itemList: Flow<List<ItemHolder>> = searchResult.combine(
        marbleRepository.favoriteChanges()
    ) { search, favorites ->
        search?.results?.map {
            val isFavorite = favorites.contains(it.characterId)
            ItemHolder(it, isFavorite)
        } ?: emptyList()
    }

    private val isLoadingLocal = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = isLoadingLocal

    fun init() {
        if (isInitialize) return
        isInitialize = true

        viewModelScope.launch {
            searchText
                .filter { it.isNotEmpty() }
                .mapLatest<String, SearchResult?> {
                    isLoadingLocal.emit(true)
                    val result = marbleRepository.search(it, 0)
                    isLoadingLocal.emit(false)
                    result
                }
                .stable()
                .collect { searchResult.emit(it) }
        }
    }

    fun onTextChanges(query: String) {
        viewModelScope.launch {
            searchText.emit(query)
        }
    }

    fun loadMore() {
        viewModelScope.launch {
            if (searchResult.value == null || searchText.value.isEmpty() || isLoadingLocal.value) {
                return@launch
            }

            isLoadingLocal.emit(true)

            val nextOffset = searchResult.value!!.offset + OFFSET_COUNT
            val loadResult = marbleRepository.search(searchText.value, nextOffset)

            val addedCardList =
                (searchResult.value!!.results + loadResult.results).distinctBy { it.characterId }

            val newSearchResult = SearchResult(loadResult.total, loadResult.offset, addedCardList)
            searchResult.emit(newSearchResult)

            isLoadingLocal.emit(false)
        }
    }

    fun onSelectCard(id: Int) {
        viewModelScope.launch {
            marbleRepository.setFavorite(id)
        }
    }

    companion object {
        private const val OFFSET_COUNT = 10
    }
}
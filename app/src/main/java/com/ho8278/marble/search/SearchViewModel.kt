package com.ho8278.marble.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ho8278.data.model.SearchResult
import com.ho8278.data.repository.MarbleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
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

    fun init() {
        if (isInitialize) return
        isInitialize = true

        viewModelScope.launch {
            searchText
                .mapLatest { marbleRepository.search(it, 0) }
                .collect { searchResult.emit(it) }
        }
    }

    fun onTextChanges(query: String) {
        viewModelScope.launch {
            searchText.emit(query)
        }
    }
}
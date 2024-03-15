package com.ho8278.marble.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ho8278.data.model.Card
import com.ho8278.data.repository.MarbleRepository
import com.ho8278.marble.common.ItemHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val marbleRepository: MarbleRepository
) : ViewModel() {

    val itemList = marbleRepository.favoriteChanges()
        .map { cards ->
            println(cards.joinToString())
            cards.map {
                ItemHolder(it, true)
            }
        }

    fun onCardClick(card: Card) {
        viewModelScope.launch {
            marbleRepository.removeFavorite(card.characterId)
        }
    }
}
package com.ho8278.marble.common

import androidx.recyclerview.widget.DiffUtil
import com.ho8278.data.model.Card

data class ItemHolder(val card: Card, val isFavorite: Boolean) {
    companion object {
        fun diffUtilCallback(): DiffUtil.ItemCallback<ItemHolder> {
            return object : DiffUtil.ItemCallback<ItemHolder>() {
                override fun areItemsTheSame(oldItem: ItemHolder, newItem: ItemHolder): Boolean {
                    return oldItem.card.characterId == newItem.card.characterId
                }

                override fun areContentsTheSame(oldItem: ItemHolder, newItem: ItemHolder): Boolean {
                    return oldItem == newItem
                }
            }
        }
    }
}
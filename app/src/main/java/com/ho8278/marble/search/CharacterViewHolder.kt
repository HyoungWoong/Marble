package com.ho8278.marble.search

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ho8278.data.model.Card
import com.ho8278.marble.databinding.ItemCharacterBinding

class CharacterViewHolder(
    private val binding: ItemCharacterBinding,
    private val onItemClick: (card: Card) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(itemHolder: ItemHolder) {
        binding.root.setOnClickListener {
            onItemClick(itemHolder.card)
        }

        Glide.with(binding.thumbnail)
            .load(itemHolder.card.thumbnail)
            .into(binding.thumbnail)

        binding.name.text = itemHolder.card.name
        binding.description.text = itemHolder.card.description

        binding.root.isSelected = itemHolder.isFavorite
    }
}
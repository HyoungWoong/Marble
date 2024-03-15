package com.ho8278.marble.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ho8278.data.model.Card
import com.ho8278.marble.databinding.ItemCharacterBinding

class MarbleCharacterAdapter(
    private val onItemClick: (card: Card) -> Unit
) : ListAdapter<ItemHolder, RecyclerView.ViewHolder>(ItemHolder.diffUtilCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            ItemCharacterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CharacterViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? CharacterViewHolder)?.onBind(getItem(position))
    }
}


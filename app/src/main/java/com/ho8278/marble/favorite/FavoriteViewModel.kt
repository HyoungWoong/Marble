package com.ho8278.marble.favorite

import androidx.lifecycle.ViewModel
import com.ho8278.data.repository.MarbleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val marbleRepository: MarbleRepository
) : ViewModel() {

}
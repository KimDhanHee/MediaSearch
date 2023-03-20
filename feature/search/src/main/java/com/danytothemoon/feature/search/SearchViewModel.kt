package com.danytothemoon.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danytothemoon.core.data.repository.MediaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
  private val mediaRepository: MediaRepository,
) : ViewModel() {
  fun searchMedia(keyword: String) {
    viewModelScope.launch {
      mediaRepository.search(keyword).collect {

      }
    }
  }
}
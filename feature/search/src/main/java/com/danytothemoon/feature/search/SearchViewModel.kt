package com.danytothemoon.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danytothemoon.core.data.model.MediaItem
import com.danytothemoon.core.data.repository.MediaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
  private val mediaRepository: MediaRepository,
) : ViewModel() {
  private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
  val uiState: StateFlow<SearchUiState> = _uiState

  fun searchMedia(keyword: String) {
    _uiState.value = SearchUiState.Loading

    viewModelScope.launch {
      combine(
        mediaRepository.searchVideo(keyword, page = 1),
        mediaRepository.searchImage(keyword, page = 1),
      ) { videoResult, imageResult ->
        videoResult.mediaList + imageResult.mediaList
      }.collect { mediaList ->
        _uiState.value = SearchUiState.Success(mediaList)
      }
    }
  }

  fun loadMoreMedia() {
  }

  fun registerInterest(mediaItem: MediaItem) {
    viewModelScope.launch {
      mediaRepository.registerInterest(mediaItem)
    }
  }

  fun deregisterInterest(mediaItem: MediaItem) {
    viewModelScope.launch {
      mediaRepository.deregisterInterest(mediaItem)
    }
  }
}
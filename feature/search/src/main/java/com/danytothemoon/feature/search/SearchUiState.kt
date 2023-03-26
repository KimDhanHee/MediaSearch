package com.danytothemoon.feature.search

import com.danytothemoon.core.data.model.MediaItem

sealed class SearchUiState {
  object Idle : SearchUiState()

  object Loading : SearchUiState()

  data class Success(
    val mediaItems: List<MediaItem>,
  ) : SearchUiState()

  object Error : SearchUiState()
}

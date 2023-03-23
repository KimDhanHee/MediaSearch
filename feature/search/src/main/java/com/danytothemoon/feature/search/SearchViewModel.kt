package com.danytothemoon.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danytothemoon.core.data.model.MediaItem
import com.danytothemoon.core.data.repository.MediaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
  private val mediaRepository: MediaRepository,
) : ViewModel() {
  private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
  val uiState: StateFlow<SearchUiState> = _uiState

  private lateinit var requestInfo: RequestInfo

  private var currentMediaList: List<MediaItem> = emptyList()

  fun searchMedia(keyword: String) {
    _uiState.value = SearchUiState.Loading

    viewModelScope.launch {
      requestInfo = RequestInfo(keyword)

      combine(
        mediaRepository.searchVideo(keyword, page = requestInfo.nextVideoPage),
        mediaRepository.searchImage(keyword, page = requestInfo.nextImagePage),
      ) { videoResult, imageResult ->
        requestInfo.updateVideoRequestable(videoResult.isMoreAvailable)
        requestInfo.updateImageRequestable(imageResult.isMoreAvailable)

        videoResult.mediaList + imageResult.mediaList
      }.collect { mediaList ->
        currentMediaList = mediaList.sortedByDescending { it.datetime }
        _uiState.value = SearchUiState.Success(currentMediaList)
      }
    }
  }

  fun loadMoreMedia() {
    _uiState.value = SearchUiState.Loading

    viewModelScope.launch {
      combine(
        when {
          requestInfo.isVideoRequestable ->
            mediaRepository.searchVideo(requestInfo.keyword, page = requestInfo.nextVideoPage)
          else -> emptyFlow()
        },
        when {
          requestInfo.isImageRequestable ->
            mediaRepository.searchImage(requestInfo.keyword, page = requestInfo.nextImagePage)
          else -> emptyFlow()
        }
      ) { videoResult, imageResult ->
        requestInfo.updateVideoRequestable(videoResult.isMoreAvailable)
        requestInfo.updateImageRequestable(imageResult.isMoreAvailable)

        videoResult.mediaList + imageResult.mediaList
      }.collect { mediaList ->
        currentMediaList = (currentMediaList + mediaList).sortedByDescending { it.datetime }
        _uiState.value = SearchUiState.Success(currentMediaList)
      }
    }
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

private class RequestInfo(val keyword: String) {
  var imagePage: Int = 0
  private var videoPage: Int = 0

  var isVideoRequestable: Boolean = true
    private set

  fun updateVideoRequestable(isRequestable: Boolean) {
    isVideoRequestable = isRequestable
  }

  var isImageRequestable: Boolean = true
    private set

  fun updateImageRequestable(isRequestable: Boolean) {
    isImageRequestable = isRequestable
  }

  val nextVideoPage: Int
    get() {
      require(isVideoRequestable) { "Video request is not allowed" }

      videoPage += 1

      return videoPage
    }

  val nextImagePage: Int
    get() {
      require(isImageRequestable) { "Image request is not allowed" }

      imagePage += 1

      return imagePage
    }
}
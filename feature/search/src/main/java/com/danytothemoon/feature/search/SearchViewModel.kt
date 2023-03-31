package com.danytothemoon.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danytothemoon.core.data.model.MediaItem
import com.danytothemoon.core.data.repository.MediaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class SearchViewModel @Inject constructor(
  private val mediaRepository: MediaRepository,
) : ViewModel() {
  private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
  val uiState: StateFlow<SearchUiState> = _uiState

  private val interestedUrlListFlow = mediaRepository.getInterestedMediaListFlow()
    .map { mediaList -> mediaList.map(MediaItem::url) }
    .stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(),
      emptyList(),
    )

  private var currentMediaList: List<MediaItem> = emptyList()

  init {
    observeInterestedMediaItem()
  }

  private fun observeInterestedMediaItem() {
    viewModelScope.launch {
      interestedUrlListFlow.collect { interestedUrlList ->
        if (_uiState.value is SearchUiState.Success) {
          _uiState.value = SearchUiState.Success(
            currentMediaList.map { it.copy(isInterested = it.url in interestedUrlList) }
          )
        }
      }
    }
  }

  private lateinit var requestInfo: RequestInfo

  fun searchMedia(keyword: String) {
    requestInfo = RequestInfo(keyword)
    currentMediaList = emptyList()

    loadMedia()
  }

  fun loadMoreMedia() {
    loadMedia()
  }

  private fun loadMedia() {
    _uiState.value = SearchUiState.Loading

    viewModelScope.launch {
      runCatching {
        getNextMediaListFlow().collect { nextMediaList ->
          currentMediaList = (currentMediaList + nextMediaList)
            .distinctBy { it.url }
            .sortedByDescending { it.datetime }

          _uiState.value = SearchUiState.Success(
            currentMediaList.map { it.copy(isInterested = it.url in interestedUrlListFlow.value) }
          )
        }
      }.onFailure {
        _uiState.value = SearchUiState.Error
      }
    }
  }

  private fun getNextMediaListFlow(): Flow<List<MediaItem>> = combine(
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
  ) { videoResponse, imageResponse ->
    requestInfo.updateVideoRequestable(videoResponse.isMoreAvailable)
    requestInfo.updateImageRequestable(imageResponse.isMoreAvailable)

    videoResponse.mediaList + imageResponse.mediaList
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
  private var imagePage: Int = 0
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
      require(isVideoRequestable) { "check isVideoRequestable first" }

      videoPage += 1

      return videoPage
    }

  val nextImagePage: Int
    get() {
      require(isImageRequestable) { "check isImageRequestable first" }

      imagePage += 1

      return imagePage
    }
}
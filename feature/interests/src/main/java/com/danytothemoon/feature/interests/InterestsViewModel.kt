package com.danytothemoon.feature.interests

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danytothemoon.core.data.model.MediaItem
import com.danytothemoon.core.data.repository.MediaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class InterestsViewModel @Inject constructor(
  private val mediaRepository: MediaRepository,
) : ViewModel() {
  val interestedMediaListFlow = mediaRepository.getInterestedMediaListFlow().stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(),
    emptyList(),
  ).map { mediaList -> mediaList.sortedByDescending { it.datetime } }

  fun deregisterInterest(mediaItem: MediaItem) {
    viewModelScope.launch {
      mediaRepository.deregisterInterest(mediaItem)
    }
  }
}
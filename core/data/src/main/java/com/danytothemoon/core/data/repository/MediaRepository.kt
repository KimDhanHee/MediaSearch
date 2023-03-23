package com.danytothemoon.core.data.repository

import com.danytothemoon.core.data.model.MediaItem
import com.danytothemoon.core.data.model.SearchResult
import kotlinx.coroutines.flow.Flow

interface MediaRepository {
  fun search(keyword: String): Flow<SearchResult>

  fun getInterestedMediaList(): Flow<List<MediaItem>>

  suspend fun registerInterest(mediaItem: MediaItem)

  suspend fun deregisterInterest(mediaItem: MediaItem)
}
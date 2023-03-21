package com.danytothemoon.core.data.repository

import com.danytothemoon.core.data.model.MediaItem
import kotlinx.coroutines.flow.Flow

interface MediaRepository {
  fun search(keyword: String): Flow<List<MediaItem>>

  fun getInterestedMediaList(): Flow<List<MediaItem>>

  suspend fun registerInterest(mediaItem: MediaItem)

  suspend fun deregisterInterest(mediaItem: MediaItem)
}
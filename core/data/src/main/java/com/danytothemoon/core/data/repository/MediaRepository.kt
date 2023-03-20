package com.danytothemoon.core.data.repository

import com.danytothemoon.core.data.model.MediaItem
import kotlinx.coroutines.flow.Flow

interface MediaRepository {
  fun search(keyword: String): Flow<List<MediaItem>>
}
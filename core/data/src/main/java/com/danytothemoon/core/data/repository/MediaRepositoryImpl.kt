package com.danytothemoon.core.data.repository

import com.danytothemoon.core.data.model.MediaItem
import com.danytothemoon.core.network.retrofit.SearchMediaNetwork
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MediaRepositoryImpl @Inject constructor(
  private val network: SearchMediaNetwork,
) : MediaRepository {
  override fun search(keyword: String): Flow<List<MediaItem>> = flow {
  }
}
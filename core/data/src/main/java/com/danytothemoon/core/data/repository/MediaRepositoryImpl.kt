package com.danytothemoon.core.data.repository

import com.danytothemoon.core.data.model.MediaItem
import com.danytothemoon.core.network.retrofit.SearchMediaNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MediaRepositoryImpl @Inject constructor(
  private val network: SearchMediaNetwork,
) : MediaRepository {
  override fun search(keyword: String): Flow<List<MediaItem>> = flow {
    val videos = network.getVideos(keyword).documents.map {
      MediaItem(url = it.url, datetime = it.datetime)
    }
    val images = network.getImages(keyword).documents.map {
      MediaItem(url = it.url, datetime = it.datetime)
    }
    val result = (videos + images).sortedByDescending { it.datetime }
    emit(result)
  }.flowOn(Dispatchers.IO)
}
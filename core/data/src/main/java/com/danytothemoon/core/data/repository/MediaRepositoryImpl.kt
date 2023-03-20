package com.danytothemoon.core.data.repository

import com.danytothemoon.core.data.model.MediaItem
import com.danytothemoon.core.datastore.UserDataPreference
import com.danytothemoon.core.network.retrofit.SearchMediaNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MediaRepositoryImpl @Inject constructor(
  private val network: SearchMediaNetwork,
  private val preference: UserDataPreference,
) : MediaRepository {
  override fun search(keyword: String): Flow<List<MediaItem>> = combine(
    flow {
      val videos = network.getVideos(keyword).documents.map { MediaItem(it.url, it.datetime) }
      emit(videos)
    },
    flow {
      val images = network.getImages(keyword).documents.map { MediaItem(it.url, it.datetime) }
      emit(images)
    },
    preference.interestedUrlSet
  ) { videos, images, interestedUrls ->
    (videos + images).sortedByDescending { it.datetime }
  }.flowOn(Dispatchers.IO)

  override suspend fun registerInterest(mediaItem: MediaItem) {
    preference.registerInterest(mediaItem.url)
  }

  override suspend fun deregisterInterest(mediaItem: MediaItem) {
    preference.deregisterInterest(mediaItem.url)
  }
}
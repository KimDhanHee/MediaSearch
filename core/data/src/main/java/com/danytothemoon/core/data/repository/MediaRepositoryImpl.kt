package com.danytothemoon.core.data.repository

import com.danytothemoon.core.data.model.MediaItem
import com.danytothemoon.core.datastore.UserDataPreference
import com.danytothemoon.core.datastore.model.InterestedMedia
import com.danytothemoon.core.network.retrofit.SearchMediaNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MediaRepositoryImpl @Inject constructor(
  private val network: SearchMediaNetwork,
  private val preference: UserDataPreference,
) : MediaRepository {
  override fun search(keyword: String): Flow<List<MediaItem>> = combine(
    flow {
      val searchedVideoList =
        network.getVideos(keyword).documents.map { MediaItem(it.url, it.datetime) }
      emit(searchedVideoList)
    },
    flow {
      val searchedImageList =
        network.getImages(keyword).documents.map { MediaItem(it.url, it.datetime) }
      emit(searchedImageList)
    },
    preference.interestedMediaListFlow
  ) { searchedVideoList, searchedImageList, interestedMediaList ->
    val interestedUrlList = interestedMediaList.map(InterestedMedia::url)
    val searchedResultList = (searchedVideoList + searchedImageList)
      .map { media -> media.copy(isInterested = media.url in interestedUrlList) }
      .sortedByDescending { it.datetime }

    searchedResultList
  }.flowOn(Dispatchers.IO)

  override fun getInterestedMediaList(): Flow<List<MediaItem>> =
    preference.interestedMediaListFlow.map { list ->
      list.map { it.toMediaItem() }
        .sortedByDescending { it.datetime }
    }

  override suspend fun registerInterest(mediaItem: MediaItem) {
    preference.registerInterest(mediaItem.url)
  }

  override suspend fun deregisterInterest(mediaItem: MediaItem) {
    preference.deregisterInterest(mediaItem.url)
  }

  private fun InterestedMedia.toMediaItem() = MediaItem(url, datetime, isInterested = true)
}
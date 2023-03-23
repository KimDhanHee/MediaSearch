package com.danytothemoon.core.data.repository

import com.danytothemoon.core.data.model.MediaItem
import com.danytothemoon.core.data.model.SearchResult
import com.danytothemoon.core.datastore.UserDataPreference
import com.danytothemoon.core.datastore.model.InterestedMedia
import com.danytothemoon.core.network.model.ImageDocument
import com.danytothemoon.core.network.model.VideoDocument
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
  override fun search(keyword: String): Flow<SearchResult> = combine(
    flow { emit(network.getVideos(keyword)) },
    flow { emit(network.getImages(keyword)) },
    preference.interestedMediaListFlow
  ) { searchedVideoResult, searchedImageResult, interestedMediaList ->
    val searchedVideoList = searchedVideoResult.documents.map { it.toMediaItem() }
    val searchedImageList = searchedImageResult.documents.map { it.toMediaItem() }
    val interestedUrlList = interestedMediaList.map(InterestedMedia::url)

    val mediaList = (searchedVideoList + searchedImageList)
      .map { media -> media.copy(isInterested = media.url in interestedUrlList) }
      .sortedByDescending { it.datetime }

    SearchResult(mediaList)
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

  private fun VideoDocument.toMediaItem() = MediaItem(url, datetime)

  private fun ImageDocument.toMediaItem() = MediaItem(url, datetime)

  private fun InterestedMedia.toMediaItem() = MediaItem(url, datetime, isInterested = true)
}
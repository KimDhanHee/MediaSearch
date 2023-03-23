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
  override fun searchVideo(keyword: String, page: Int): Flow<SearchResult> = combine(
    flow { emit(network.getVideos(keyword, page = page)) },
    preference.interestedMediaListFlow
  ) { searchedVideoResult, interestedMediaList ->
    val searchedVideoList = searchedVideoResult.documents.map { it.toMediaItem() }
    val interestedUrlList = interestedMediaList.map(InterestedMedia::url)

    val mediaList = searchedVideoList
      .map { media -> media.copy(isInterested = media.url in interestedUrlList) }

    SearchResult(mediaList, isMoreAvailable = !searchedVideoResult.meta.isEnd)
  }.flowOn(Dispatchers.IO)

  override fun searchImage(keyword: String, page: Int): Flow<SearchResult> = combine(
    flow { emit(network.getImages(keyword, page = page)) },
    preference.interestedMediaListFlow
  ) { searchedImageResult, interestedMediaList ->
    val searchedImageList = searchedImageResult.documents.map { it.toMediaItem() }
    val interestedUrlList = interestedMediaList.map(InterestedMedia::url)

    val mediaList = searchedImageList
      .map { media -> media.copy(isInterested = media.url in interestedUrlList) }

    SearchResult(mediaList, isMoreAvailable = !searchedImageResult.meta.isEnd)
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
package com.danytothemoon.core.data.repository

import com.danytothemoon.core.data.di.IoDispatcher
import com.danytothemoon.core.data.model.MediaItem
import com.danytothemoon.core.data.model.SearchResult
import com.danytothemoon.core.datastore.UserDataPreference
import com.danytothemoon.core.datastore.model.InterestedMedia
import com.danytothemoon.core.network.model.ImageDocument
import com.danytothemoon.core.network.model.VideoDocument
import com.danytothemoon.core.network.retrofit.SearchMediaNetwork
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class MediaRepositoryImpl @Inject constructor(
  private val network: SearchMediaNetwork,
  private val preference: UserDataPreference,
  @IoDispatcher private val dispatcher: CoroutineDispatcher
) : MediaRepository {
  override fun searchVideo(keyword: String, page: Int): Flow<SearchResult> = flow {
    val response = network.getVideos(keyword, page = page)
    val searchedVideoList = response.documents.map { it.toMediaItem() }

    emit(SearchResult(searchedVideoList, isMoreAvailable = !response.meta.isEnd))
  }.flowOn(dispatcher)

  override fun searchImage(keyword: String, page: Int): Flow<SearchResult> = flow {
    val response = network.getImages(keyword, page = page)
    val searchedImageResult = response.documents.map { it.toMediaItem() }

    emit(SearchResult(searchedImageResult, isMoreAvailable = !response.meta.isEnd))
  }.flowOn(dispatcher)

  override fun getInterestedMediaListFlow(): Flow<List<MediaItem>> =
    preference.interestedMediaListFlow.map { list ->
      list.map { it.toMediaItem() }
        .sortedByDescending { it.datetime }
    }.flowOn(dispatcher)

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
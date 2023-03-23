package com.danytothemoon.core.network.model

import com.danytothemoon.core.network.model.util.DatetimeSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchVideoResult(
  val meta: MetaInfo,
  val documents: List<VideoDocument>,
)

@Serializable
data class MetaInfo(
  @SerialName("total_count") val totalCount: Int,
  @SerialName("pageable_count") val pageableCount: Int,
  @SerialName("is_end") val isEnd: Boolean,
)

@Serializable
data class VideoDocument(
  @SerialName("thumbnail") val url: String,
  @Serializable(DatetimeSerializer::class)
  val datetime: LocalDateTime,
)

@Serializable
data class SearchImageResult(
  val meta: MetaInfo,
  val documents: List<ImageDocument>,
)

@Serializable
data class ImageDocument(
  @SerialName("thumbnail_url") val url: String,
  @Serializable(DatetimeSerializer::class)
  val datetime: LocalDateTime,
)
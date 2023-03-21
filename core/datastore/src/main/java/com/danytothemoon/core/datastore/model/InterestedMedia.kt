package com.danytothemoon.core.datastore.model

import com.danytothemoon.core.datastore.model.util.DatetimeSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class InterestedMedia(
  val url: String,
  @Serializable(DatetimeSerializer::class)
  val datetime: LocalDateTime,
)
package com.danytothemoon.core.data.model

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import java.time.format.DateTimeFormatter

data class MediaItem(
  val url: String,
  val datetime: LocalDateTime,
  val isInterested: Boolean = false,
) {
  val key: String = "${url}_${datetime}"
  val dateStr: String = datetime.format("yyyy-MM-dd")
  val timeStr: String = datetime.format("HH:mm:ss")

  private fun LocalDateTime.format(pattern: String): String =
    this.toJavaLocalDateTime().format(DateTimeFormatter.ofPattern(pattern))
}
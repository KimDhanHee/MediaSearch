package com.danytothemoon.core.data.model

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import java.time.format.DateTimeFormatter

data class MediaItem(
  val url: String,
  val datetime: LocalDateTime,
) {
  val dateStr: String = datetime.format("yyyy-MM-dd")
  val timeStr: String = datetime.format("HH:mm:ss")
}

fun LocalDateTime.format(pattern: String) =
  this.toJavaLocalDateTime().format(DateTimeFormatter.ofPattern(pattern))
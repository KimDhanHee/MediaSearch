package com.danytothemoon.core.network.model.util

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object DatetimeSerializer : KSerializer<LocalDateTime> {
  override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
    serialName = "LocalDateTime",
    kind = PrimitiveKind.STRING,
  )

  override fun deserialize(decoder: Decoder): LocalDateTime =
    decoder.decodeString().toInstant().toLocalDateTime(TimeZone.currentSystemDefault())

  override fun serialize(encoder: Encoder, value: LocalDateTime) =
    encoder.encodeString(value.toString())
}
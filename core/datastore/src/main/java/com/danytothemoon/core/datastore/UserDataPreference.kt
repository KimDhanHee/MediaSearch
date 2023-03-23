package com.danytothemoon.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.danytothemoon.core.datastore.model.InterestedMedia
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDataPreference @Inject constructor(
  @ApplicationContext private val context: Context,
) {
  private val Context.datastore: DataStore<Preferences> by preferencesDataStore("user_data")

  private val prefInterestedMediaList = stringPreferencesKey("interested_media_list")

  private val Preferences.interestedMediaList: List<InterestedMedia>
    get() = Json.decodeFromString(this[prefInterestedMediaList] ?: "[]")

  val interestedMediaListFlow =
    context.datastore.data.map { preferences -> preferences.interestedMediaList }

  suspend fun registerInterest(key: String, url: String) {
    context.datastore.edit { preferences ->
      val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
      val newInterest = InterestedMedia(key, url, datetime = now)

      preferences[prefInterestedMediaList] =
        Json.encodeToString(preferences.interestedMediaList + newInterest)
    }
  }

  suspend fun deregisterInterest(key: String) {
    context.datastore.edit { preferences ->
      val target = preferences.interestedMediaList.find { it.key == key } ?: return@edit

      preferences[prefInterestedMediaList] =
        Json.encodeToString(preferences.interestedMediaList - target)
    }
  }
}
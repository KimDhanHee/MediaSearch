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

  suspend fun registerInterest(media: InterestedMedia) {
    context.datastore.edit { preferences ->
      preferences[prefInterestedMediaList] =
        Json.encodeToString(preferences.interestedMediaList + media)
    }
  }

  suspend fun deregisterInterest(media: InterestedMedia) {
    context.datastore.edit { preferences ->
      preferences[prefInterestedMediaList] =
        Json.encodeToString(preferences.interestedMediaList - media)
    }
  }
}
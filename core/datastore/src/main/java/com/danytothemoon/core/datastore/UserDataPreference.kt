package com.danytothemoon.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDataPreference @Inject constructor(
  @ApplicationContext private val context: Context,
) {
  private val Context.datastore: DataStore<Preferences> by preferencesDataStore("user_data")

  private val prefInterestedUrlSet = stringSetPreferencesKey("interested_url_set")

  private val Preferences.interestedUrlSet: Set<String>
    get() = this[prefInterestedUrlSet] ?: emptySet()

  val interestedUrlSet = context.datastore.data.map { preferences -> preferences.interestedUrlSet }

  suspend fun registerInterest(url: String) {
    context.datastore.edit { preferences ->
      preferences[prefInterestedUrlSet] = preferences.interestedUrlSet + url
    }
  }

  suspend fun deregisterInterest(url: String) {
    context.datastore.edit { preferences ->
      preferences[prefInterestedUrlSet] = preferences.interestedUrlSet - url
    }
  }
}
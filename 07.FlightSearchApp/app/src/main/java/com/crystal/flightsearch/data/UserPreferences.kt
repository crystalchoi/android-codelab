package com.crystal.flightsearch.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>
){

    val storedQuery: Flow<String> = dataStore.data
        .catch {
            if(it is IOException) {
                Log.e(TAG, "Error reading preferences.", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            Log.d(TAG, "pref: ${preferences[STORED_QUERY]?.toString()}")
            preferences[STORED_QUERY] ?: ""
        }

    suspend fun saveStoredQuery(storedQuery: String) {
        dataStore.edit { preferences ->
            preferences[STORED_QUERY] = storedQuery
        }
    }

    private companion object {
        val STORED_QUERY = stringPreferencesKey("stored_query")
        const val TAG = "UserPreferencesRepo"
    }
}
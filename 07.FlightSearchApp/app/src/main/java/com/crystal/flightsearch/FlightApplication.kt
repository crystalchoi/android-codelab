package com.crystal.flightsearch

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.crystal.flightsearch.data.AppContainer
import com.crystal.flightsearch.data.AppDataContainer
import com.crystal.flightsearch.data.FlightDatabase
import com.crystal.flightsearch.data.UserPreferencesRepository


private const val FAVORITE_PREFERENCE_NAME = "favorite_preferences"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = FAVORITE_PREFERENCE_NAME
)

class FlightApplication: Application() {
    val database: FlightDatabase by lazy { FlightDatabase.getDatabase(this) }

    lateinit var container: AppContainer

    // for DataStore Preferences
    lateinit var userPreferencesRepository: UserPreferencesRepository

    override fun onCreate() {
        super.onCreate()
        userPreferencesRepository = UserPreferencesRepository(dataStore)
        container = AppDataContainer(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        FlightDatabase.destroyDatabase()
    }
}

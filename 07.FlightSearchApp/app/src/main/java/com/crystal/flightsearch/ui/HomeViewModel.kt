package com.crystal.flightsearch.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.room.Dao
import com.crystal.flightsearch.BusScheduleApplication
import com.crystal.flightsearch.FlightApplication
import com.crystal.flightsearch.data.Airport
import com.crystal.flightsearch.data.AirportDao
import com.crystal.flightsearch.data.FavoriteDao
import kotlinx.coroutines.flow.Flow


class HomeViewModel(
    private val airportDao: AirportDao,
    private val favoriteDao: FavoriteDao,
): ViewModel() {
    // Get full bus schedule from Room DB
    fun getAllAirports(): Flow<List<Airport>> = airportDao.getAllAirports()
    // Get bus schedule based on the stop name from Room DB
    fun getAirportName(name: String): Flow<List<Airport>> =
        airportDao.getAirportName(name)

    companion object {
        val factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as FlightApplication)
                HomeViewModel(application.database.airportDao(), application.database.favoriteDao())
                // Preferences 도... 여기에 집어 넣을 것...
            }
        }
    }
}
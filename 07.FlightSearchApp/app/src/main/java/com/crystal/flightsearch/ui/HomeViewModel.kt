package com.crystal.flightsearch.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.room.Dao
import com.crystal.flightsearch.FlightApplication
import com.crystal.flightsearch.data.Airport
import com.crystal.flightsearch.data.AirportDao
import com.crystal.flightsearch.data.Favorite
import com.crystal.flightsearch.data.FavoriteDao
import com.crystal.flightsearch.data.FlightRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(
    savedStateHandle: SavedStateHandle,
    private val flightRepository: FlightRepository
): ViewModel() {

    val homeUiState: StateFlow<HomeUiState> =
        flightRepository.getAllAirports().map { HomeUiState(it) }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = HomeUiState()
        )
    fun getAllAirports(): Flow<List<Airport>> = flightRepository.getAllAirports()
    // Get bus schedule based on the stop name from Room DB
    fun getAirportName(name: String): Flow<List<Airport>> = flightRepository.getAirportName(name)
    fun getFavoriteMatchedCode(code: String): Flow<List<Favorite>>
        = flightRepository.getFavoriteMatchedCode(code)

    companion object {
        val factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as FlightApplication)
                HomeViewModel(
                    this.createSavedStateHandle(),
                    application.container.flightRepository)
            }
        }

        private const val TIMEOUT_MILLIS = 5_000L
    }
}


data class HomeUiState(val itemList: List<Airport> = listOf())
package com.crystal.flightsearch.ui

import android.util.Log
import androidx.compose.runtime.R
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.crystal.flightsearch.FlightApplication
import com.crystal.flightsearch.data.Airport
import com.crystal.flightsearch.data.AirportDao
import com.crystal.flightsearch.data.Favorite
import com.crystal.flightsearch.data.FavoriteDao
import com.crystal.flightsearch.data.FlightRepository
import com.crystal.flightsearch.data.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private const val TAG = "HomeViewModel"

class HomeViewModel(
    savedStateHandle: SavedStateHandle,
    private val flightRepository: FlightRepository,
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {

    val homeUiState: StateFlow<HomeUiState> =
        flightRepository.getAllAirports().map { HomeUiState(it) }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = HomeUiState()
        )

    val queryUiState: StateFlow<StoredQueryUiState> = userPreferencesRepository.storedQuery.map {
            Log.d(TAG, "StateFlow: storedQuery : $it")
            StoredQueryUiState(it)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = StoredQueryUiState()
        )

    fun getAllAirports(): Flow<List<Airport>> = flightRepository.getAllAirports()
    // Get bus schedule based on the stop name from Room DB
    fun getAirportName(name: String): Flow<List<Airport>> = flightRepository.getAirportName(name)

    fun getFavoriteMatchedCode(code: String): Flow<List<Favorite>>
        = flightRepository.getFavoriteMatchedCode(code)

    fun getAllFavorites(): Flow<List<Favorite>> //= flightRepository.getAllFavorites()
     = flowOf(
        listOf(
            Favorite(1, "TRDES", "GFSD"),
            Favorite(2, "TRDES", "GFSD"),
        )
     )


    fun saveStoredQuery(query: String) {
        viewModelScope.launch {
            try {
                userPreferencesRepository.saveStoredQuery(query)
            } catch (e: Exception) {
                Log.d(TAG, "saveStoredQuery ${e.message}")
            }
        }
    }


    companion object {
        val factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as FlightApplication)
                HomeViewModel(
                    this.createSavedStateHandle(),
                    application.container.flightRepository,
                    application.userPreferencesRepository)
            }
        }

        private const val TIMEOUT_MILLIS = 5_000L
    }
}


data class HomeUiState(val itemList: List<Airport> = listOf())

data class StoredQueryUiState(
    val storedQuery: String = "",
)

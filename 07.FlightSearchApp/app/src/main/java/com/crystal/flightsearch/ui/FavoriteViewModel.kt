package com.crystal.flightsearch.ui

import android.content.ClipData
import android.util.Log
import androidx.compose.ui.input.key.Key.Companion.F
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.crystal.flightsearch.FlightApplication
import com.crystal.flightsearch.data.Airport
import com.crystal.flightsearch.data.Favorite
import com.crystal.flightsearch.data.FlightRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


private const val TAG = "FavViewModel"

class FavoriteViewModel(
    savedStateHandle: SavedStateHandle,
    private val flightRepository: FlightRepository
): ViewModel() {

    private val favoriteAll: Flow<FavoriteUiState> =
        flightRepository.getAllFavorites().map { FavoriteUiState(it) }


    fun insert(favorite: Favorite) {
        viewModelScope.launch {
            try {
//                flightRepository.insertItem(favorite)
            } catch (e: Exception) {
                Log.d(TAG, "${e.message}")
            }
        }
    }

    fun update(favorite: Favorite, starOn: Boolean) {
        viewModelScope.launch {
            try {
//                flightRepository.updateItem(favorite)
            } catch (e: Exception) {
                Log.d(TAG, "${e.message}")
            }
        }
    }

    fun getAllFavorites(): Flow<List<Favorite>> //= flightRepository.getAllFavorites()
            = flowOf(
        listOf(
            Favorite(1, "MAD4501394803", "LAS4q09458q0"),
            Favorite(2, "LAV59403582", "MADrq40q8r"),
            Favorite(3, "INC59403582", "SEOrq40q8r"),
        )
    )

    fun getFavoriteMatchedCode(code: String): Flow<List<Favorite>>
//            = flightRepository.getFavoriteMatchedCode(code)
            = flowOf(
        listOf(
            Favorite(1, "MAD4501394803", "LAS4q09458q0"),
            Favorite(2, "LAV59403582", "MADrq40q8r"),
            Favorite(3, "INC59403582", "SEOrq40q8r"),
        )
    )

    fun update(favorite: Favorite) {

    }

    companion object {
        val factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as FlightApplication)
                FavoriteViewModel(
                    this.createSavedStateHandle(),
                    application.container.flightRepository)
            }
        }

        private const val TIMEOUT_MILLIS = 5_000L
    }
}



data class FavoriteUiState(
    val itemList: List<Favorite> = listOf()
)

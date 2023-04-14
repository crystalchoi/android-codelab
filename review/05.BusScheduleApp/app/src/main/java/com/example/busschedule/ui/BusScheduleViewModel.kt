/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.busschedule.ui

import android.content.ClipData
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.busschedule.BusScheduleApplication
import com.example.busschedule.busScheduleApplication
import com.example.busschedule.data.BusSchedule
import com.example.busschedule.data.ItemsRepository
import kotlinx.coroutines.flow.*

class BusScheduleViewModel(savedStateHandle: SavedStateHandle,
                           private val itemsRepository: ItemsRepository): ViewModel() {


    val homeUiState: StateFlow<HomeUiState> =
        itemsRepository.getAllItemsStream().map { HomeUiState(it) }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = HomeUiState()
        )

    // Get example bus schedule from Room DB
//    fun getFullSchedule(): Flow<List<BusSchedule>> {
//
//    }


    // Get example bus schedule by stop
    fun getScheduleFor(stopName: String): Flow<List<BusSchedule>> =
        flowOf(
            listOf(
                BusSchedule(1, "Example Street", 0),
                BusSchedule(2, "XXXXX Street", 0),
                BusSchedule(3, "YYYYY Street", 0)
            )
        )

    companion object {
        val factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                BusScheduleViewModel(this.createSavedStateHandle(),
                    busScheduleApplication().container.itemsRepository)
            }
        }
        private const val TIMEOUT_MILLIS = 5_000L
    }
}


data class HomeUiState(val itemList: List<BusSchedule> = listOf())
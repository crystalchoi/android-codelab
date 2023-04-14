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

package com.example.inventory.ui.item

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.ItemsRepository
import kotlinx.coroutines.flow.*


private const val TAG = "EditViewModel"

/**
 * ViewModel to retrieve and update an item from the [ItemsRepository]'s data source.
 */
class ItemEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val itemsRepository: ItemsRepository
) : ViewModel() {



    private val itemId: Int = checkNotNull(savedStateHandle[ItemEditDestination.itemIdArg])

    /**
     * Holds current item ui state
     */


    val uiState: StateFlow<ItemUiState> =  itemsRepository.getItemStream(itemId)
        .filterNotNull()
        .map {  it.toItemUiState() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(ItemEditViewModel.TIMEOUT_MILLIS),
            initialValue = ItemUiState(id = itemId)
        )

    var currentUiState by mutableStateOf(uiState.value)
        private set

    fun updateUiState(newState: ItemUiState) {
        currentUiState = newState.copy()
    }

    suspend fun saveItem() {
        if (currentUiState.isValid()) {
            itemsRepository.updateItem(currentUiState.toItem())
        } else {
            Log.d(TAG, "invalid $currentUiState")
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

}

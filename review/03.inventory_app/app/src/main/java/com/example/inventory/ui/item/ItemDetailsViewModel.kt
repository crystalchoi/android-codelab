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

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.ItemsRepository
import kotlinx.coroutines.flow.*

/**
 * ViewModel to retrieve, update and delete an item from the data source.
 */
class ItemDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val itemsRepository: ItemsRepository
) : ViewModel() {

    private val itemId: Int = checkNotNull(savedStateHandle[ItemDetailsDestination.itemIdArg])

    val uiState: StateFlow<ItemUiState> =  itemsRepository.getItemStream(itemId)
        .filterNotNull()
        .map {  it.toItemUiState() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = ItemUiState(id = itemId)
        )

    var currentUiState by mutableStateOf(uiState.value)
        private set

    suspend fun deleteItem() {
        if (uiState.value.isValid()) {
            itemsRepository.deleteItem(uiState.value.toItem())
        }
    }

    suspend fun sellItem(soldItemUiState: ItemUiState) {
        val leftItemUiState = uiState.value.copy(quantity =
        (uiState.value.quantity.toInt() - soldItemUiState.quantity.toInt()).toString())

        if (leftItemUiState.isValid()) {
            itemsRepository.updateItem(uiState.value.toItem())
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

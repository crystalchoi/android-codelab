package com.crystal.flightsearch.data

import android.content.ClipData
import kotlinx.coroutines.flow.Flow

interface FlightRepository {
    fun getAllAirports(): Flow<List<Airport>>
    fun getAirport(id: Int): Flow<Airport?>
    fun getAirportName(name: String): Flow<List<Airport>>
    suspend fun insertItem(item: Airport)
    suspend fun deleteItem(item: Airport)
    suspend fun updateItem(item: Airport)

    fun getAllFavorites(): Flow<List<Favorite>>
    fun getFavorite(id: Int): Flow<Favorite?>
    fun getFavoriteMatchedCode(code: String): Flow<List<Favorite>>
    suspend fun insertItem(item: Favorite)
    suspend fun deleteItem(item: Favorite)
    suspend fun updateItem(item: Favorite)
}
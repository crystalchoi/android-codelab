package com.crystal.flightsearch.data

import android.content.ClipData
import kotlinx.coroutines.flow.Flow

class OfflineFlightRepository(
    private val airportDao: AirportDao,
    private val favoriteDao: FavoriteDao,
) : FlightRepository {
    override fun getAllAirports(): Flow<List<Airport>> = airportDao.getAllAirports()
    override fun getAirport(id: Int): Flow<Airport?> = airportDao.getAirport(id)
    override fun getAirportName(name: String): Flow<List<Airport>>
        = airportDao.getAirportName(name)
    override suspend fun insertItem(item: Airport) = airportDao.insert(item)
    override suspend fun deleteItem(item: Airport) = airportDao.delete(item)
    override suspend fun updateItem(item: Airport) = airportDao.update(item)

    override fun getAllFavorites(): Flow<List<Favorite>> = favoriteDao.getAllFavorites()
    override fun getFavorite(id: Int): Flow<Favorite?> = favoriteDao.getFavorite(id)
    override fun getFavoriteMatchedCode(code: String): Flow<List<Favorite>>
        = favoriteDao.getFavoriteMatchedCode(code)
    override suspend fun insertItem(item: Favorite) = favoriteDao.insert(item)
    override suspend fun deleteItem(item: Favorite) = favoriteDao.delete(item)
    override suspend fun updateItem(item: Favorite) = favoriteDao.update(item)
}
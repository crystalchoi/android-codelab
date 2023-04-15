package com.crystal.flightsearch.data

import android.content.ClipData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AirportDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Airport)
    @Update
    suspend fun update(item: Airport)
    @Delete
    suspend fun delete(item: Airport)

    @Query("SELECT * from airport WHERE id = :id")
    fun getAirport(id: Int): Flow<Airport?>

    @Query("SELECT * from airport WHERE name = :name || iata_code = name")
    fun getAirportName(name: String): Flow<List<Airport>>

    @Query("SELECT * from airport ORDER BY name ASC")
    fun getAllAirports(): Flow<List<Airport>>
}

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Favorite)
    @Update
    suspend fun update(item: Favorite)
    @Delete
    suspend fun delete(item: Favorite)


    @Query("SELECT * from favorite WHERE id = :id")
    fun getFavorite(id: Int): Flow<Favorite?>

    @Query("SELECT * from favorite WHERE departure_code = :code || destination_code = :code")
    fun getFavoriteCode(code: String): Flow<List<Favorite>>

    @Query("SELECT * from favorite ORDER BY name ASC")
    fun getAllFavorites(): Flow<List<Favorite>>
}
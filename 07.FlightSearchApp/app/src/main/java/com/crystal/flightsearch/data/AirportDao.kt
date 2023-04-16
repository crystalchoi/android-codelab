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


    @Query("SELECT * from airport WHERE name LIKE  :name " +
            "OR iata_code LIKE :name ORDER BY name ASC")
    fun getAirportName(name: String): Flow<List<Airport>>

    @Query("SELECT * from airport ORDER BY name ASC")
    fun getAllAirports(): Flow<List<Airport>>
}


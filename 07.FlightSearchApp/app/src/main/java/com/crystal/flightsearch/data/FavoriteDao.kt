package com.crystal.flightsearch.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow


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

    @Query("SELECT * from favorite ORDER BY departure_code ASC")
    fun getAllFavorites(): Flow<List<Favorite>>
}
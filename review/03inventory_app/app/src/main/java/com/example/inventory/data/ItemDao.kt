package com.example.inventory.data

import androidx.room.*
import androidx.room.Insert


@Dao
interface ItemDao {
    @Insert
    suspend fun insert(item: Item)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Item)

    @Update
    suspend fun update(item: Item)

    @Delete
    suspend fun delete(item: Item)
}
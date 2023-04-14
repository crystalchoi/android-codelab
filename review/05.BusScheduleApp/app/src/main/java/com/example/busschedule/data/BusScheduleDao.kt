package com.example.busschedule.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BusScheduleDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: BusSchedule)
    @Update
    suspend fun update(item: BusSchedule)
    @Delete
    suspend fun delete(item: BusSchedule)

    @Query("SELECT * from Schedule WHERE stop_name = :stopName")
    fun getStopName(stopName: String): Flow<List<BusSchedule>>

    @Query("SELECT * from Schedule WHERE id = :id")
    fun getItem(id: Int): Flow<BusSchedule>

    @Query("SELECT * from Schedule ORDER BY stop_name ASC")
    fun getAllItems(): Flow<List<BusSchedule>>
}
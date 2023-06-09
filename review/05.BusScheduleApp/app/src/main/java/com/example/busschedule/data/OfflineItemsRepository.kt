package com.example.busschedule.data

import android.content.ClipData

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

import kotlinx.coroutines.flow.Flow


class OfflineItemsRepository(private val itemDao: BusScheduleDao) : ItemsRepository {
    override fun getAllItemsStream(): Flow<List<BusSchedule>> {
        return itemDao.getAllItems()
    }

    override fun getItemStream(id: Int): Flow<BusSchedule?> = itemDao.getItem(id)
    override fun getStopNameStream(stopName: String): Flow<List<BusSchedule>> = itemDao.getStopName(stopName)

    override fun getStopName(stopName: String): Flow<List<BusSchedule>> = itemDao.getStopName(stopName)

    override suspend fun insertItem(item: BusSchedule) = itemDao.insert(item)

    override suspend fun deleteItem(item: BusSchedule) = itemDao.delete(item)

    override suspend fun updateItem(item: BusSchedule) = itemDao.update(item)

}
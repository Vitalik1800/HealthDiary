package com.vs18.healthdiary.repository

import com.vs18.healthdiary.App
import com.vs18.healthdiary.data.entity.HealthEntry

class HealthRepository {
    private val dao = App.database.healthDao()

    fun getAllEntries() = dao.getAll()

    fun getEntriesInRange(start: Long, end: Long) = dao.getByRange(start, end)

    suspend fun insert(entry: HealthEntry) = dao.insert(entry)

    suspend fun update(entry: HealthEntry) = dao.update(entry)

    suspend fun delete(entry: HealthEntry) = dao.delete(entry)
}
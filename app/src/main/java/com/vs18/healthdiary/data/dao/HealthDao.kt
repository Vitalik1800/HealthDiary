package com.vs18.healthdiary.data.dao

import androidx.room.*
import com.vs18.healthdiary.data.entity.HealthEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface HealthDao {

    @Query("SELECT * FROM health_entries ORDER BY date DESC")
    fun getAll(): Flow<List<HealthEntry>>

    @Query("SELECT * FROM health_entries WHERE date BETWEEN :start AND :end ORDER BY date ASC")
    fun getByRange(start: Long, end: Long): Flow<List<HealthEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: HealthEntry)

    @Update
    suspend fun update(entry: HealthEntry)

    @Delete
    suspend fun delete(entry: HealthEntry)
}
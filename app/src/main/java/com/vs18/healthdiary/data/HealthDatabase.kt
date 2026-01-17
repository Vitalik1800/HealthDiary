package com.vs18.healthdiary.data

import androidx.room.*
import com.vs18.healthdiary.data.dao.HealthDao
import com.vs18.healthdiary.data.entity.HealthEntry

@Database(entities = [HealthEntry::class], version = 1, exportSchema = false)
abstract class HealthDatabase : RoomDatabase() {
    abstract fun healthDao(): HealthDao
}
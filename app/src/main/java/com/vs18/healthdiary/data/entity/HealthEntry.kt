package com.vs18.healthdiary.data.entity

import androidx.room.*

@Entity(tableName = "health_entries")
data class HealthEntry (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "date")
    val date: Long,

    @ColumnInfo(name = "sleep_hours")
    val sleepHours: Float,

    @ColumnInfo(name = "mood")
    val mood: Int,

    @ColumnInfo(name = "steps")
    val steps: Int,

    @ColumnInfo(name = "weight")
    val weight: Float?
)
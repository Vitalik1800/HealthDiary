package com.vs18.healthdiary

import android.app.Application
import androidx.room.Room
import com.vs18.healthdiary.data.HealthDatabase

class App : Application() {
    companion object {
        lateinit var database: HealthDatabase
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(this, HealthDatabase::class.java, "health_db").build()
    }
}
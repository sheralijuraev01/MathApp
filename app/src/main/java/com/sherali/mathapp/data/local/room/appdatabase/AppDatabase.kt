package com.sherali.mathapp.data.local.room.appdatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sherali.mathapp.data.local.room.dao.AppDao
import com.sherali.mathapp.data.local.room.entity.ScoreEntity

@Database(entities = [ScoreEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getAppDao(): AppDao
}
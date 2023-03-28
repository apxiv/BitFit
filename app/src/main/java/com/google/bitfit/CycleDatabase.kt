package com.google.bitfit

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [CycleEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class CycleDatabase: RoomDatabase() {
    abstract fun cycleDao(): CycleDao

    companion object {
        @Volatile
        private var INSTANCE: CycleDatabase? = null

        fun getInstance(context: Context): CycleDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                CycleDatabase::class.java, "Cycle-db"
            ).build()
    }
}
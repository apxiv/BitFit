package com.google.bitfit

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CycleDao {
    @Query("SELECT * FROM cycle_table ORDER BY start_date DESC")
    fun getAll(): Flow<List<CycleEntity>>

    @Insert
    fun insert(cycle: CycleEntity)

    @Delete
    fun delete(cycle: CycleEntity)

}
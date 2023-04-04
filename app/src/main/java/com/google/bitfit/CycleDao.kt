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

    @Query("SELECT COUNT(is_painful) FROM cycle_table WHERE is_painful=1")
    fun getNumberOfPainfulDays(): Int

    @Query("SELECT COUNT(*) FROM cycle_table")
    fun getSize(): Int

    @Query("SELECT COUNT(*) FROM cycle_table WHERE feeling=0")
    fun getSad(): Int

    @Query("SELECT COUNT(*) FROM cycle_table WHERE feeling=1")
    fun getNeutral(): Int

    @Query("SELECT COUNT(*) FROM cycle_table WHERE feeling=2")
    fun getHappy(): Int

    @Insert
    fun insert(cycle: CycleEntity)

    @Delete
    fun delete(cycle: CycleEntity)

}
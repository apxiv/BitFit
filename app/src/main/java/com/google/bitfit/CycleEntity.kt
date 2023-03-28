package com.google.bitfit

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "cycle_table")

data class CycleEntity(
    @ColumnInfo(name = "start_date") val startPeriod: Date,
    @ColumnInfo(name = "end_date") val endPeriod: Date,
    @ColumnInfo(name = "is_painful") val isPainful: Boolean = false,
    @ColumnInfo(name = "feeling") val feelings: Int = 1,
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
)

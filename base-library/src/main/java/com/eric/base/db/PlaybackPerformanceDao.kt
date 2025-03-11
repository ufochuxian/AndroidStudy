package com.eric.base.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PlaybackPerformanceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(log: PlaybackPerformanceEntity)

    @Query("SELECT * FROM playback_performance WHERE url = :url")
    fun getByUrl(url: String): PlaybackPerformanceEntity?
}

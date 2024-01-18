package com.sherali.mathapp.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sherali.mathapp.data.local.room.entity.ScoreEntity

@Dao
interface AppDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveScore(score: ScoreEntity)
    @Query("SELECT * FROM SCORE_TABLE ORDER BY score DESC")
    suspend fun getAllScore(): List<ScoreEntity>

    @Query("SELECT * FROM SCORE_TABLE WHERE levelKey = :key ")
    suspend fun getScore(key:String): ScoreEntity

    @Query("SELECT MAX(score) FROM SCORE_TABLE WHERE category = :category")
    suspend fun getMaxByLevelKey(category: String): Int

    @Query("SELECT MAX(score) FROM SCORE_TABLE")
    suspend fun getMaxScore(): Int
}
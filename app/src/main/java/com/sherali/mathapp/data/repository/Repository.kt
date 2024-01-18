package com.sherali.mathapp.data.repository

import com.sherali.mathapp.data.local.room.entity.ScoreEntity

interface Repository {

    suspend fun saveName(name: String)
    suspend fun getName(): String
    suspend fun saveIndex(index: Int)
    suspend fun getIndex(): Int

    suspend fun saveStatus(status: Boolean)
    suspend fun getStatus(): Boolean

    suspend fun saveScore(scoreEntity: ScoreEntity)

    suspend fun getAllScore(): List<ScoreEntity>
    suspend fun getScore(key:String): ScoreEntity

    suspend fun getMaxByCategory(category: String): Int

    suspend fun getMaxScore():Int

}
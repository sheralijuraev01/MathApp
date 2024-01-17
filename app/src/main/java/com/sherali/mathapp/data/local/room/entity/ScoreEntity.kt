package com.sherali.mathapp.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sherali.mathapp.util.Constants.TABLE_NAME


@Entity(tableName = TABLE_NAME)
data class ScoreEntity(
    @PrimaryKey
    val levelKey: String,
    val category: String,
    val levelName: String,
    val time: String,
    val score: Int
)

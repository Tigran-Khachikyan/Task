package com.example.task.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "INFO")
data class Info(
    val albumId: Int,
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "ID")
    val id: Int,
    val title: String,
    val url: String,
    val thumbnailUrl: String,
)

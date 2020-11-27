package com.example.task.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ARTICLES")
data class ModelDb(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "_id")
    val id: String,
    val webPublicationDate: String,
    val webUrl: String,
    val sectionName: String,
    val webTitle: String,
    var isFavourite: Boolean
)


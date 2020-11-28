package com.example.task.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "ALBUMS")
data class Album (
    val userId: Int,
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "ID")
    val id: Int,
    val title: String,
): Parcelable

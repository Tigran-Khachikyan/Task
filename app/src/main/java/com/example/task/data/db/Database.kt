package com.example.task.data.db

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.task.model.Album
import com.example.task.model.Info

@androidx.room.Database(
    entities = [Album::class, Info::class],
    version = 1
)
abstract class Database : RoomDatabase() {
    abstract fun getTaskDao(): TaskDao

    companion object {
        @Volatile
        private var instance: Database? = null

        operator fun invoke(context: Context): Database {
            return instance ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    Database::class.java,
                    "TASK_DB"
                ).build()
                this.instance = instance
                return instance
            }
        }
    }
}
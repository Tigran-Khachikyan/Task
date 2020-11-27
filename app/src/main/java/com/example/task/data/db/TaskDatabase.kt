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
abstract class TaskDatabase : RoomDatabase() {
    abstract fun getTaskDao(): TaskDao

    companion object {
        @Volatile
        private var instance: TaskDatabase? = null

        operator fun invoke(context: Context): TaskDatabase {
            return instance ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskDatabase::class.java,
                    "TASK_DB"
                ).build()
                this.instance = instance
                return instance
            }
        }
    }
}
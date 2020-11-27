package com.example.task.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.task.model.Album
import com.example.task.model.Info

@Dao
interface TaskDao {

    @Query("SELECT * FROM ALBUMS")
    suspend fun getSavedAlbums(): List<Album>?

    @Query("SELECT * FROM INFO WHERE albumId =:albumId")
    suspend fun getInfoFromAlbum(albumId: Int): List<Info>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAlbum(album: Album)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveInfo(info: List<Info>)

    @Query("DELETE FROM ALBUMS WHERE ID =:id")
    suspend fun removeAlbum(id: Int)

    @Query("DELETE FROM INFO WHERE albumId =:albumId")
    suspend fun removeInfo(albumId: Int)

}
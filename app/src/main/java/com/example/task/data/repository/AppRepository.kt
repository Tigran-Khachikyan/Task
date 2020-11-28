package com.example.task.data.repository

import com.example.task.model.Album
import com.example.task.model.Info
import retrofit2.Response


interface AppRepository {
    suspend fun getSavedAlbums(): List<Album>?
    suspend fun getAlbumsFromServer(): Response<List<Album>?>
    suspend fun getAlbumInfoFromDb(albumId: Int): List<Info>?
    suspend fun getAlbumInfoFromServer(albumId: Int): Response<List<Info>?>
    suspend fun save(album: Album, info: List<Info>)
    suspend fun remove(albumId: Int)
}
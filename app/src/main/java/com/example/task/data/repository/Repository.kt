package com.example.task.data.repository

import android.content.Context
import com.example.task.data.api.RetrofitService
import com.example.task.data.api.TaskApiService
import com.example.task.data.db.TaskDao
import com.example.task.data.db.TaskDatabase
import com.example.task.model.Album
import com.example.task.model.Info
import retrofit2.Response

class Repository(private val context: Context) : AppRepository {

    private val taskApiService: TaskApiService by lazy { RetrofitService.create() }
    private val taskDao: TaskDao by lazy { TaskDatabase.invoke(context).getTaskDao() }

    override suspend fun getSavedAlbums(): List<Album>? = taskDao.getSavedAlbums()

    override suspend fun getAlbumsFromServer(): Response<List<Album>?> =
        taskApiService.getAlbumsAsync().await()

    override suspend fun getAlbumInfoFromDb(albumId: Int): List<Info>? =
        taskDao.getInfoFromAlbum(albumId)

    override suspend fun getAlbumInfoFromServer(albumId: Int): Response<List<Info>?> =
        taskApiService.getAlbumInfoAsync(albumId).await()

    override suspend fun save(album: Album, info: List<Info>) {
        taskDao.saveAlbum(album)
        taskDao.saveInfo(info)
    }

    override suspend fun remove(albumId: Int) {
        taskDao.removeAlbum(albumId)
        taskDao.removeInfo(albumId)
    }
}


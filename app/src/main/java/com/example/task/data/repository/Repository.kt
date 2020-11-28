package com.example.task.data.repository

import android.app.DownloadManager
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.LiveData
import com.example.task.data.api.RetrofitService
import com.example.task.data.api.TaskApiService
import com.example.task.data.db.TaskDao
import com.example.task.data.db.TaskDatabase
import com.example.task.model.Album
import com.example.task.model.Info
import kotlinx.coroutines.Dispatchers
import retrofit2.Response
import java.io.File
import java.lang.Exception

class Repository(
    private val context: Context,
) : AppRepository {

    private val taskApiService: TaskApiService by lazy { RetrofitService.create() }
    private val taskDao: TaskDao by lazy { TaskDatabase.invoke(context).getTaskDao() }
    private val downloadManager by lazy { context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager }


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

    override suspend fun remove(albumId: Int): Boolean {
        return try {
            taskDao.removeAlbum(albumId)
            taskDao.removeInfo(albumId)
            true
        } catch (ex: Exception) {
            false
        }
    }
}


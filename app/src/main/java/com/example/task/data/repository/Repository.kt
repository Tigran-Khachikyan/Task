package com.example.task.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.task.data.api.RetrofitService
import com.example.task.data.api.TaskApiService
import com.example.task.data.db.TaskDao
import com.example.task.data.db.TaskDatabase
import com.example.task.model.Album
import com.example.task.model.Info
import retrofit2.Response
import java.lang.Exception

class Repository(
    private val context: Context,
) : AppRepository {

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

    override suspend fun remove(albumId: Int): Boolean {
        return try {
            taskDao.removeAlbum(albumId)
            taskDao.removeInfo(albumId)
            true
        } catch (ex: Exception) {
            false
        }
    }


    /*
    *//* override suspend fun updateDb() {

         if (hasNetwork(context)) {
             val newsResponse = safeApiCall(
                 call = { newsApi.getNewsAsync().await() },
                 errorMessage = context.getString(R.string.error)
             )
             val articles = newsResponse?.response?.results
             if (articles != null && articles.isNotEmpty()) {
                 newsDao.clearAll()
                 newsDao.insert(articles)
             }
         }
     }*//*

    override suspend fun getNewsApi(page: Int): List<ModelApi>? {
        var articles: List<ModelApi>? = null
        if (hasNetwork(context)) {
            val newsResponse = safeApiCall(
                call = { newsApi.getNewsAsync(page).await() },
                errorMessage = context.getString(R.string.error)
            )
            newsResponse?.response?.results?.let { articles = it }
        }
        return articles
    }

    override fun getSavedArticle(id: String): ModelDb? = newsDao.getArticle(id)

    override fun getFavourites(): LiveData<List<ModelDb>> = newsDao.getAll()

    override suspend fun save(article: ModelDb) = newsDao.insert(article)

    override suspend fun remove(id: String) = newsDao.removeFavourite(id)

    override suspend fun clearAll() = newsDao.clearAll()*/
}

package com.example.task.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TaskDao {

/*    @Query("SELECT * FROM ARTICLES")
    fun getAll(): LiveData<List<ModelDb>>

    @Query("SELECT * FROM ARTICLES WHERE _id =:id")
    fun getArticle(id: String): ModelDb

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(articles: ModelDb)

    @Query("DELETE FROM ARTICLES WHERE _id =:id")
    suspend fun removeFavourite(id: String)

    @Query("DELETE FROM ARTICLES")
    suspend fun clearAll()*/

    /*@Query("UPDATE ARTICLES SET isFavourite = 0")
    suspend fun removeAllFavourites()*/

    /*@Query("SELECT * FROM ARTICLES WHERE isFavourite = 1")
    fun getFavourites(): LiveData<List<ModelApi>>*/

    /* @Query("UPDATE ARTICLES SET isFavourite=0 WHERE _id = :id")
     suspend fun removeFavourite(id: String)*/

    /* @Query("UPDATE ARTICLES SET isFavourite=1 WHERE _id = :id")
     suspend fun setFavourite(id: String)*/
}
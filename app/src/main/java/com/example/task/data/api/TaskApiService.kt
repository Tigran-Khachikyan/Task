package com.example.task.data.api

import com.example.task.model.Album
import com.example.task.model.Info
import io.reactivex.Observable
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TaskApiService {

    // https://jsonplaceholder.typicode.com/albums
    @GET("albums")
    fun getAlbumsAsync(): Deferred<Response<List<Album>?>>

    // https://jsonplaceholder.typicode.com/photos?albumId=100
    @GET("photos")
    fun getAlbumInfoAsync(@Query("albumId") id: Int): Deferred<Response<List<Info>?>>


/*    // https://jsonplaceholder.typicode.com/albums
    @GET("albums")
    fun getAlbumsAsync(): Observable<List<Album>>

    // https://jsonplaceholder.typicode.com/photos?albumId=100
    @GET("photos")
    fun getAlbumInfoAsync(@Query("albumId") id: Int): Observable<List<Info>>*/

}
package com.example.task.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.task.data.repository.Repository
import com.example.task.model.Album
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoadAlbumsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: Repository by lazy { Repository(application) }

    private val albumList = MutableLiveData<List<Album>?>()

    private fun loadAlbums() {
        Log.d("kbdja644", "loadAlbums")
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getAlbumsFromServer()
            if (response.isSuccessful) {
                val albums = response.body()
                withContext(Dispatchers.Main) {
                    albumList.value = albums
                    error.value = null
                }
            } else {
                val message = response.message()
                withContext(Dispatchers.Main) {
                    error.value = message
                }
            }
        }
    }

    private val loadTrigger = MutableLiveData<Unit>()

    fun loadOrRefresh() {
        loadTrigger.value = Unit
    }

    val error = MutableLiveData<String?>()
    val albums: LiveData<List<Album>?> = loadTrigger.switchMap {
        loadAlbums()
        albumList
    }

}
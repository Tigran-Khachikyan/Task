package com.example.task.ui.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.task.data.repository.Repository
import com.example.task.isNetworkAvailable
import com.example.task.model.Album
import com.example.task.ui.IoTransactionsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoadAlbumsViewModel(application: Application) : AndroidViewModel(application) {

    private val context by lazy { application }
    private val repository: Repository by lazy { Repository(application) }

    private val albumList = MutableLiveData<List<Album>?>()

    private fun loadAlbums() {
        if (!isNetworkAvailable(context)) {
            status.value = IoTransactionsState.NO_NETWORK
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.getAlbumsFromServer()
                if (response.isSuccessful) {
                    val albums = response.body()
                    withContext(Dispatchers.Main) {
                        albumList.value = albums
                        status.value = IoTransactionsState.LOADING_SUCCEED
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        status.value = IoTransactionsState.LOADING_ERROR
                    }
                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    status.value = IoTransactionsState.LOADING_ERROR
                }
            }
        }
    }

    private val loadTrigger = MutableLiveData<Unit>()

    fun loadOrRefresh() {
        loadTrigger.value = Unit
    }

    val status = MutableLiveData<IoTransactionsState>()
    val albums: LiveData<List<Album>?> = loadTrigger.switchMap {
        loadAlbums()
        albumList
    }

}
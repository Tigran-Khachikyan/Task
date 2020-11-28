package com.example.task.ui.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.task.data.repository.Repository
import com.example.task.model.Album
import com.example.task.ui.IoTransactionsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class SavedAlbumsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: Repository by lazy { Repository(application) }

    private val albumList = MutableLiveData<List<Album>?>()

    private fun loadAlbums() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val saved = repository.getSavedAlbums()
                withContext(Dispatchers.Main) {
                    albumList.value = saved
                    status.value = IoTransactionsState.LOADING_SUCCEED
                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    status.value = IoTransactionsState.LOADING_ERROR
                }
            }
        }
    }

    fun load() {
        refreshTrigger.value = Unit
    }

    val status = MutableLiveData<IoTransactionsState>()
    fun remove(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.remove(id)
                withContext(Dispatchers.Main) {
                    status.value = IoTransactionsState.REMOVING_SUCCEED
                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    status.value = IoTransactionsState.REMOVING_FAILED
                }
            }
        }
    }

    private val refreshTrigger = MutableLiveData<Unit>()

    val albums: LiveData<List<Album>?> = refreshTrigger.switchMap {
        loadAlbums()
        albumList
    }


}
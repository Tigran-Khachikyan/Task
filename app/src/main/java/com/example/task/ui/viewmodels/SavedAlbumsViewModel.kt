package com.example.task.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.task.data.repository.Repository
import com.example.task.model.Album
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SavedAlbumsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: Repository by lazy { Repository(application) }

    private val albumList = MutableLiveData<List<Album>?>()

    private fun loadAlbums() {
        Log.d("kbdja644", "loadAlbums")
        viewModelScope.launch(Dispatchers.IO) {
            val saved = repository.getSavedAlbums()
            withContext(Dispatchers.Main) {
                albumList.value = saved
            }
        }
    }

    fun loadOrRefresh() {
        refreshTrigger.value = Unit
    }

    private val removeSucceed = MutableLiveData<Boolean>()
    fun remove(id: Int): LiveData<Boolean> {
        viewModelScope.launch(Dispatchers.IO) {
            val succeed  = repository.remove(id)
            withContext(Dispatchers.Main){
                removeSucceed.value = succeed
            }
        }
        return removeSucceed
    }

    private val refreshTrigger = MutableLiveData<Unit>()

    val albums: LiveData<List<Album>?> = refreshTrigger.switchMap {
        loadAlbums()
        albumList
    }


}
package com.example.task.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.task.data.repository.Repository
import com.example.task.model.Album
import com.example.task.model.Info
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class InfoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: Repository by lazy { Repository(application) }

    private val infoSavedLoaded by lazy { MutableLiveData<Pair<Boolean, List<Info>?>>() }
    private val loadTrigger by lazy { MutableLiveData<Int>() }
    val error by lazy { MutableLiveData<String?>() }


    private fun loadInfo(albumId: Int) {
        Log.d("kbdja644", "albumId : $albumId")
        viewModelScope.launch(Dispatchers.IO) {

            // first trying to get info from db if exists
            val infoFromDb = repository.getAlbumInfoFromDb(albumId)
            infoFromDb?.let {
                if (it.isNotEmpty()) {
                    Log.d("kbdja644", "infoFromDb.size() : ${it.size}")

                    withContext(Dispatchers.Main) {
                        error.value = null
                        infoSavedLoaded.value = Pair(true, it)
                    }
                    return@launch
                }
            }

            // trying to get info from server
            val response = repository.getAlbumInfoFromServer(albumId)
            if (response.isSuccessful) {
                val info = response.body()
                Log.d("kbdja644", "info.size() : ${info?.size}")

                withContext(Dispatchers.Main) {
                    error.value = null
                    infoSavedLoaded.value = Pair(false, info)
                }
            } else {
                val message = response.message()
                withContext(Dispatchers.Main) {
                    error.value = message
                }
            }
        }
    }

    fun save(album: Album, info: List<Info>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.save(album, info)
        }
    }

    fun loadOrRefresh(albumId: Int) {
        loadTrigger.value = albumId
    }

    val albumInfo: LiveData<Pair<Boolean, List<Info>?>> = loadTrigger.switchMap {
        loadInfo(it)
        infoSavedLoaded
    }

    private val removeSucceed = MutableLiveData<Boolean>()
    fun remove(albumId: Int): LiveData<Boolean> {
        viewModelScope.launch(Dispatchers.IO) {
            val succeed  = repository.remove(albumId)
            withContext(Dispatchers.Main){
                removeSucceed.value = succeed
            }
        }
        return removeSucceed
    }

}
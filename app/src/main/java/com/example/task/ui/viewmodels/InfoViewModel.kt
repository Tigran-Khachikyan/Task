package com.example.task.ui.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.task.data.repository.Repository
import com.example.task.isNetworkAvailable
import com.example.task.model.Album
import com.example.task.model.Info
import com.example.task.ui.IoTransactionsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class InfoViewModel(application: Application) : AndroidViewModel(application) {

    private val context by lazy { application }
    private val repository: Repository by lazy { Repository(application) }

    private val infoSavedLoaded by lazy { MutableLiveData<Pair<Boolean, List<Info>?>>() }
    val status by lazy { MutableLiveData<IoTransactionsState>() }

    private val isTriggerOnLoad by lazy { MutableLiveData<Pair<Boolean, Int>>() }

    fun load(albumId: Int) {
        isTriggerOnLoad.value = Pair(false, albumId)
    }

    fun refresh(albumId: Int) {
        isTriggerOnLoad.value = Pair(true, albumId)
    }


    private fun loadInfo(onlyNew: Boolean, albumId: Int) {
        viewModelScope.launch(Dispatchers.IO) {

            if (!onlyNew) {
                // first trying to get info from db if exists
                val infoFromDb = repository.getAlbumInfoFromDb(albumId)
                infoFromDb?.let {
                    if (it.isNotEmpty()) {
                        withContext(Dispatchers.Main) {
                            status.value = IoTransactionsState.LOADING_SUCCEED
                            infoSavedLoaded.value = Pair(true, it)
                        }
                        return@launch
                    }
                }
            }

            // trying to get info from server
            // check network state
            withContext(Dispatchers.Main) {
                if (!isNetworkAvailable(context)) {
                    status.value = IoTransactionsState.NO_NETWORK
                    return@withContext
                } else {
                    withContext(Dispatchers.IO) {
                        try {
                            val response = repository.getAlbumInfoFromServer(albumId)
                            if (response.isSuccessful) {
                                val info = response.body()
                                withContext(Dispatchers.Main) {
                                    status.value = IoTransactionsState.LOADING_SUCCEED
                                    infoSavedLoaded.value = Pair(false, info)
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
            }
        }
    }

    fun save(album: Album, info: List<Info>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.save(album, info)
        }
    }

    val albumInfo: LiveData<Pair<Boolean, List<Info>?>> = isTriggerOnLoad.switchMap {
        loadInfo(it.first, it.second)
        infoSavedLoaded
    }

    fun remove(albumId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.remove(albumId)
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

}
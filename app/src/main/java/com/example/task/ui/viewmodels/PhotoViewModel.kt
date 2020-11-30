package com.example.task.ui.viewmodels

import android.app.Application
import android.app.DownloadManager
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.task.isNetworkAvailable
import com.example.task.ui.IoTransactionsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class PhotoViewModel(application: Application) : AndroidViewModel(application) {

    private val context by lazy { application }
    private val downloadManager by lazy { application.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager }
    private val directory by lazy { File(Environment.DIRECTORY_PICTURES) }
    val status by lazy { MutableLiveData<IoTransactionsState>() }

    private fun getRequest(url: String): DownloadManager.Request {
        if (!directory.exists()) directory.mkdirs()
        val downloadUri = Uri.parse(url)
        return DownloadManager.Request(downloadUri).apply {
            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle(url.substring(url.lastIndexOf("/") + 1))
                .setDescription("")
                .setDestinationInExternalPublicDir(
                    directory.toString(),
                    url.substring(url.lastIndexOf("/") + 1)
                )
        }
    }

    fun downloadImage(url: String) {
        if (!isNetworkAvailable(context)) {
            status.value = IoTransactionsState.NO_NETWORK
        } else {
            viewModelScope.launch(Dispatchers.IO) {

                val downloadId = downloadManager.enqueue(getRequest(url))
                val query = DownloadManager.Query().setFilterById(downloadId)
                var downloading = true
                while (downloading) {
                    val cursor: Cursor = downloadManager.query(query)
                    cursor.moveToFirst()
                    if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_FAILED) {
                        downloading = false
                        withContext(Dispatchers.Main) {
                            status.value = IoTransactionsState.DOWNLOADING_FAILED
                        }
                    }
                    if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                        downloading = false
                        withContext(Dispatchers.Main) {
                            status.value = IoTransactionsState.DOWNLOADING_SUCCEED
                        }
                    }
                    cursor.close()
                }
            }
        }
    }

}
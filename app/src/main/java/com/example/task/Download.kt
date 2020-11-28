package com.example.task

import android.app.DownloadManager
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.LiveData
import java.io.File

class ImageDownloadManager {

    companion object {

        private val directory = File(Environment.DIRECTORY_PICTURES)

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




        private fun statusMessage(url: String, directory: File, status: Int): String? {
            var msg = ""
            msg = when (status) {
                DownloadManager.STATUS_FAILED -> "Download has been failed, please try again"
                DownloadManager.STATUS_PAUSED -> "Paused"
                DownloadManager.STATUS_PENDING -> "Pending"
                DownloadManager.STATUS_RUNNING -> "Downloading..."
                DownloadManager.STATUS_SUCCESSFUL -> "Image downloaded successfully in $directory" + File.separator + url.substring(
                    url.lastIndexOf("/") + 1
                )
                else -> "There's nothing to download"
            }
            return msg
        }

    }
}

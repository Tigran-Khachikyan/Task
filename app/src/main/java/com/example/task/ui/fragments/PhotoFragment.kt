package com.example.task.ui.fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.task.BUNDLE_KEY_INFO
import com.example.task.R
import com.example.task.REQUEST_PERMISSION_CODE
import com.example.task.model.Info
import com.example.task.ui.MainActivity
import com.example.task.ui.hide
import com.example.task.ui.show
import com.example.task.ui.showStatus
import com.example.task.ui.viewmodels.ActivityViewModel
import com.example.task.ui.viewmodels.PhotoViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_photo.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class PhotoFragment : Fragment(), CoroutineScope {

    private val job by lazy { Job() }
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    private lateinit var photoViewModel: PhotoViewModel
    private lateinit var activityViewModel: ActivityViewModel
    private val info: Info? by lazy { arguments?.getParcelable(BUNDLE_KEY_INFO) }
    private lateinit var activity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        photoViewModel = ViewModelProvider(this).get(PhotoViewModel::class.java)
        activityViewModel = ViewModelProvider(activity).get(ActivityViewModel::class.java)
        return inflater.inflate(R.layout.fragment_photo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        activityViewModel.permissionState().observe(viewLifecycleOwner, { result ->
            result?.let { if (it && info != null) downloadImage(info!!.url) }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    private fun initViews() {
        info?.run {
            tvTitlePhoto.text = title
            Picasso.get()
                .load(url)
                .placeholder(R.drawable.ic_image_placeholder)
                .into(ivPhotoLarge)
            btnDownload.setOnClickListener { downloadImage(url) }
        }
    }

    private fun downloadImage(url: String) {

        if (ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_PERMISSION_CODE
            )
            return
        }

        progressPhoto.show()
        tvStatusPhoto.showStatus(R.string.downloading)
        btnDownload.isEnabled = false
        photoViewModel.downloadImage(url).observe(viewLifecycleOwner, { status ->
            status?.let {
                if (it) finishDownloadingSuccessfully()
                else showDownloadError()
            }
        })
    }

    private fun finishDownloadingSuccessfully() {
        launch {
            progressPhoto.hide()
            tvStatusPhoto.hide()
            btnDownload.animate().alpha(0f).setDuration(1000).start()
            lottie_download.setAnimation(R.raw.lottie_download)
            lottie_download.visibility = View.VISIBLE
            delay(3500)
            lottie_download.visibility = View.GONE
            delay(300)
        }
    }

    private fun showDownloadError() {
        Toast.makeText(requireContext(), R.string.downloadingFailed, Toast.LENGTH_LONG).show()
        progressPhoto.hide()
        tvStatusPhoto.hide()
    }

}
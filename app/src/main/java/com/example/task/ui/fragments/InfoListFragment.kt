package com.example.task.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.task.BUNDLE_KEY_ALBUM
import com.example.task.BUNDLE_KEY_FROM_SAVED_FOLDER
import com.example.task.BUNDLE_KEY_INFO
import com.example.task.R
import com.example.task.adapters.InfoAdapter
import com.example.task.databinding.FragmentInfoListBinding
import com.example.task.model.Album
import com.example.task.model.Info
import com.example.task.ui.*
import com.example.task.ui.viewmodels.InfoViewModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class InfoListFragment() : Fragment(), CoroutineScope {

    private val job: Job by lazy { Job() }
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var viewModel: InfoViewModel
    private lateinit var binding: FragmentInfoListBinding
    private val album: Album? by lazy { arguments?.getParcelable(BUNDLE_KEY_ALBUM) }
    private val fromSavedFolder: Boolean? by lazy {
        arguments?.getBoolean(BUNDLE_KEY_FROM_SAVED_FOLDER)
    }
    private lateinit var adapter: InfoAdapter
    private var infoList: List<Info>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_info_list, container, false)
        binding = FragmentInfoListBinding.bind(v)
        viewModel = ViewModelProvider(this).get(InfoViewModel::class.java)
        return v
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        load()

        viewModel.albumInfo.observe(viewLifecycleOwner, {
            if (it.first) showSavedInfo(it.second)   // from database
            else showLoadedInfo(it.second)           //from server
        })
        viewModel.error.observe(viewLifecycleOwner, { showError(it) })
    }

    private fun initViews() {

        binding.swipeRefreshInfo.setOnRefreshListener { refresh() }
        binding.recInfo.setHasFixedSize(true)
        adapter = InfoAdapter(null) { openPhoto(it) }
        binding.recInfo.adapter = adapter
        fromSavedFolder?.let { removable ->
            if (removable) binding.btnSaveRemove.makeRemoving() else binding.btnSaveRemove.makeSaver()
            binding.btnSaveRemove.setOnClickListener { if (removable) remove() else save() }
        }
    }

    private fun openPhoto(info: Info?) {
        info?.let {
            findNavController().navigate(
                R.id.nav_photo,
                Bundle().apply {
                    putParcelable(BUNDLE_KEY_INFO, info)
                }
            )
        }
    }

    private fun load() {
        binding.progress.show()
        binding.tvStatus.showStatus(R.string.loading)
        album?.run {
            viewModel.loadOrRefresh(id)
        }
    }

    private fun showSavedInfo(info: List<Info>?) {
        info?.let {
            infoList = info
            binding.progress.hide()
            binding.tvStatus.hide()
            binding.swipeRefreshInfo.isRefreshing = false
            adapter.setAlbums(it)
            binding.icStatusSaved.visibility = View.VISIBLE
            fromSavedFolder?.let {
                binding.btnSaveRemove.visibility = if (it) View.VISIBLE else View.GONE
            }
        }
    }

    private fun showLoadedInfo(info: List<Info>?) {
        info?.let {
            infoList = info
            binding.progress.hide()
            binding.tvStatus.hide()
            binding.swipeRefreshInfo.isRefreshing = false
            adapter.setAlbums(it)
            binding.icStatusSaved.visibility = View.GONE
            binding.btnSaveRemove.visibility = View.VISIBLE
        }
    }

    private fun showError(message: String?) {
        message?.let {
            binding.progress.hide()
            binding.swipeRefreshInfo.isRefreshing = false
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            binding.tvStatus.showStatus(R.string.error_loading)
            binding.btnSaveRemove.visibility = View.GONE
        }
    }

    private fun refresh() {
        binding.tvStatus.showStatus(R.string.refreshing)
        album?.run {
            viewModel.loadOrRefresh(id)
        }
    }

    private fun save() {
        infoList?.let {
            if (album != null) {
                viewModel.save(album!!, it)
                launch {
                    binding.btnSaveRemove.isEnabled = false
                    binding.lottieSaveRemove.setAnimation(R.raw.lottie_saved)
                    binding.lottieSaveRemove.visibility = View.VISIBLE
                    binding.btnSaveRemove.animate().alpha(0f).setDuration(1500).start()
                    delay(3500)
                    binding.lottieSaveRemove.visibility = View.GONE
                    delay(300)
                    binding.icStatusSaved.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun remove() {
        album?.run {
            binding.progress.show()
            binding.tvStatus.showStatus(R.string.removing)
            binding.btnSaveRemove.isEnabled = false
            viewModel.remove(id).observe(viewLifecycleOwner, { succeed ->
                if (succeed) finishRemoving()
                else showError()
            })
        }
    }

    private fun showError() {
        binding.progress.hide()
        binding.tvStatus.hide()
        binding.btnSaveRemove.isEnabled = true
        Toast.makeText(requireContext(), R.string.error_removing, Toast.LENGTH_LONG).show()
    }

    private fun finishRemoving() {
        binding.progress.hide()
        binding.tvStatus.hide()
        launch {
            binding.lottieSaveRemove.setAnimation(R.raw.lottie_remove)
            binding.lottieSaveRemove.visibility = View.VISIBLE
            binding.btnSaveRemove.animate().alpha(0f).setDuration(1500).start()
            delay(3000)
            binding.icStatusSaved.visibility = View.GONE
            delay(500)
            binding.lottieSaveRemove.visibility = View.GONE
            delay(300)
            activity?.onBackPressed()
        }
    }


}
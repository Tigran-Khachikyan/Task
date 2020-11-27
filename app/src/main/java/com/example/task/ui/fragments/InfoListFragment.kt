package com.example.task.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.task.BUNDLE_KEY_ALBUM
import com.example.task.R
import com.example.task.adapters.InfoAdapter
import com.example.task.databinding.FragmentInfoListBinding
import com.example.task.model.Album
import com.example.task.model.Info
import com.example.task.ui.hide
import com.example.task.ui.show
import com.example.task.ui.showStatus
import com.example.task.ui.viewmodels.InfoViewModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class InfoListFragment : Fragment(), CoroutineScope {

    private val job: Job by lazy { Job() }
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var viewModel: InfoViewModel
    private lateinit var binding: FragmentInfoListBinding
    private val album: Album? by lazy { arguments?.getParcelable(BUNDLE_KEY_ALBUM) }
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
        adapter = InfoAdapter(null) {

        }
        binding.recInfo.adapter = adapter

        binding.btnSave.setOnClickListener { save() }
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
            binding.btnSave.visibility = View.GONE
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
            binding.btnSave.visibility = View.VISIBLE
        }
    }

    private fun showError(message: String?) {
        message?.let {
            binding.progress.hide()
            binding.swipeRefreshInfo.isRefreshing = false
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            binding.tvStatus.showStatus(R.string.error_loading)
            binding.btnSave.visibility = View.GONE
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
                    binding.btnSave.isEnabled = false
                    binding.lottieSave.visibility = View.VISIBLE
                    binding.btnSave.animate().alpha(0f).setDuration(1500).start()
                    delay(3500)
                    binding.lottieSave.visibility = View.GONE
                    delay(300)
                    binding.icStatusSaved.visibility = View.VISIBLE
                }
            }
        }
    }


}
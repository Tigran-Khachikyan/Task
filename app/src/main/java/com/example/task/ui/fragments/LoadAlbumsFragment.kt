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
import com.example.task.R
import com.example.task.adapters.AlbumsAdapter
import com.example.task.databinding.FragmentAlbumBinding
import com.example.task.model.Album
import com.example.task.ui.hide
import com.example.task.ui.show
import com.example.task.ui.showStatus
import com.example.task.ui.viewmodels.LoadAlbumsViewModel

class LoadAlbumsFragment : Fragment() {

    private lateinit var binding: FragmentAlbumBinding
    private lateinit var adapter: AlbumsAdapter
    private lateinit var viewModel: LoadAlbumsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_album, container, false)
        binding = FragmentAlbumBinding.bind(v)
        viewModel = ViewModelProvider(this).get(LoadAlbumsViewModel::class.java)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        load()
        viewModel.albums.observe(viewLifecycleOwner, { showAlbums(it) })
        viewModel.error.observe(viewLifecycleOwner, { showError(it) })
    }

    private fun initViews() {
        binding.swipeRefreshAlbums.setOnRefreshListener { refresh() }
        binding.recAlbums.setHasFixedSize(true)
        adapter = AlbumsAdapter.LoadAlbumsAdapter(null) { album ->
            findNavController().navigate(
                R.id.nav_info,
                Bundle().apply { putParcelable(BUNDLE_KEY_ALBUM, album) }
            )
        }
        binding.recAlbums.adapter = adapter
    }

    private fun load() {
        binding.progress.show()
        binding.tvStatus.showStatus(R.string.loading)
        viewModel.loadOrRefresh()
    }

    private fun showAlbums(albums: List<Album>?) {
        albums?.let {
            binding.progress.hide()
            binding.tvStatus.hide()
            binding.swipeRefreshAlbums.isRefreshing = false
            adapter.setAlbums(it)
        }
    }

    private fun showError(message: String?) {
        message?.let {
            binding.progress.hide()
            binding.swipeRefreshAlbums.isRefreshing = false
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            binding.tvStatus.showStatus(R.string.error_loading)
        }
    }

    private fun refresh() {
        binding.tvStatus.showStatus(R.string.refreshing)
        viewModel.loadOrRefresh()
    }


}
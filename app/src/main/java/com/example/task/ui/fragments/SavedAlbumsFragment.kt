package com.example.task.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.task.BUNDLE_KEY_ALBUM
import com.example.task.BUNDLE_KEY_FROM_SAVED_FOLDER
import com.example.task.R
import com.example.task.adapters.AlbumsAdapter
import com.example.task.databinding.FragmentAlbumBinding
import com.example.task.model.Album
import com.example.task.ui.hide
import com.example.task.ui.show
import com.example.task.ui.showStatus
import com.example.task.ui.viewmodels.SavedAlbumsViewModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class SavedAlbumsFragment : Fragment() {

    private lateinit var binding: FragmentAlbumBinding
    private lateinit var adapter: AlbumsAdapter
    private lateinit var viewModel: SavedAlbumsViewModel
    private var deletedAlbumPosition: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_album, container, false)
        binding = FragmentAlbumBinding.bind(v)
        viewModel = ViewModelProvider(this).get(SavedAlbumsViewModel::class.java)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        load()
        viewModel.albums.observe(viewLifecycleOwner, { showAlbums(it) })
    }

    private fun initViews() {
        binding.swipeRefreshAlbums.isEnabled = false
        binding.recAlbums.setHasFixedSize(true)
        adapter = AlbumsAdapter.SavedAlbumsAdapter(
            null, { open(it) }) { pos, id ->
            deletedAlbumPosition = pos
            remove(id)
        }
        binding.recAlbums.adapter = adapter
    }

    private fun open(album: Album?) {
        findNavController().navigate(
            R.id.nav_info,
            Bundle().apply {
                putParcelable(BUNDLE_KEY_ALBUM, album)
                putBoolean(BUNDLE_KEY_FROM_SAVED_FOLDER, true)
            }
        )
    }

    private fun load() {
        binding.progress.show()
        binding.tvStatus.showStatus(R.string.loading)
        viewModel.loadOrRefresh()
        Log.d("kbdja644", "load()")
    }

    private fun showAlbums(albums: List<Album>?) {
        if (albums != null && albums.isNotEmpty())
            binding.tvStatus.hide()
        else
            binding.tvStatus.showStatus(R.string.noSavedAlbums)

        binding.progress.hide()
        adapter.setAlbums(albums)
    }

    private fun showError() {
        binding.progress.hide()
        binding.tvStatus.hide()
        adapter.enableIconRemove()
        Toast.makeText(requireContext(), R.string.error_removing, Toast.LENGTH_LONG).show()
    }

    private fun remove(albumId: Int) {
        binding.progress.show()
        binding.tvStatus.showStatus(R.string.removing)
        viewModel.remove(albumId).observe(viewLifecycleOwner, { succeed ->
            if (succeed) finishRemoving()
            else showError()
        })
    }

    private fun finishRemoving() {
        deletedAlbumPosition?.let {
            binding.progress.hide()
            binding.tvStatus.hide()
            val wasTheLastAlbum = adapter.removeAlbum(it)
            wasTheLastAlbum?.let { if (it) binding.tvStatus.showStatus(R.string.noSavedAlbums) }
            Toast.makeText(requireContext(), R.string.successRemoved, Toast.LENGTH_SHORT).show()
            deletedAlbumPosition = null
        }
    }

}
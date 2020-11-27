/*
package com.example.task.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.task.R
import com.example.task.adapters.AlbumsAdapter
import com.example.task.databinding.FragmentAlbumBinding
import com.example.task.model.Album
import com.example.task.ui.AlbumsView
import com.example.task.ui.presenters.AllAlbumsPresenter
import com.example.task.ui.presenters.BaseAlbumsPresenter
import com.example.task.ui.presenters.SavedAlbumsPresenter
import moxy.MvpAppCompatFragment

class AlbumsFragmentMvp(private val onlySaved: Boolean) : MvpAppCompatFragment(), AlbumsView {

    private lateinit var presenter: BaseAlbumsPresenter
    private lateinit var binding: FragmentAlbumBinding
    private lateinit var adapter: AlbumsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        presenter = if (onlySaved) SavedAlbumsPresenter() else AllAlbumsPresenter()
        val v = inflater.inflate(R.layout.fragment_album, container, false)
        binding = FragmentAlbumBinding.bind(v)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.swipeRefreshAlbums.setOnRefreshListener { presenter.refresh() }
        binding.recAlbums.setHasFixedSize(true)
        adapter = AlbumsAdapter(
            arrayListOf(), onlySaved,
            { id -> presenter.openAlbum(id) },
            { id -> presenter.saveOrRemove(id) }
        )
        binding.recAlbums.adapter = adapter
    }

    override fun showAlbumList(albums: List<Album>) {
        adapter.setAlbums(albums)
    }

    override fun showProgress() {
        Log.d("jhjsavd64", "Fragment showProgress")
        binding.progress.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding.progress.visibility = View.GONE
    }

    override fun showStatus(textRes: Int) {
        Log.d("jhjsavd64", "Fragment showStatus")
        binding.tvStatus.setText(textRes)
        binding.tvStatus.visibility = View.VISIBLE
    }

    override fun hideStatus() {
        binding.tvStatus.visibility = View.GONE
    }
}*/

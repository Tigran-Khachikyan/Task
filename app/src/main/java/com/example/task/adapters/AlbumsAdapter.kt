package com.example.task.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.task.R
import com.example.task.databinding.HolderAlbumBinding
import com.example.task.model.Album

sealed class AlbumsAdapter(
    private var albums: List<Album>?,
    private val onlySaved: Boolean,
    private var open: (Album?) -> Unit,
    private val remove: ((Int, Int) -> Unit)? = null
) :
    RecyclerView.Adapter<AlbumsAdapter.Holder>() {

    private var iconEnabled: Boolean = true
    fun enableIconRemove() {
        iconEnabled = true
    }

    class LoadAlbumsAdapter(
        albums: List<Album>?,
        open: (Album?) -> Unit
    ) : AlbumsAdapter(albums, false, open)

    class SavedAlbumsAdapter(
        albums: List<Album>?,
        open: (Album?) -> Unit,
        remove: ((Int, Int) -> Unit)
    ) : AlbumsAdapter(albums, true, open, remove)

    fun setAlbums(albums: List<Album>?) {
        this.albums = albums
        iconEnabled = true
        notifyDataSetChanged()
    }

    // return true if it was the last album
    fun removeAlbum(pos: Int): Boolean? {
        return albums?.run {
            (this as MutableList).removeAt(pos)
            notifyItemRemoved(pos)
            isEmpty()
        }
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = HolderAlbumBinding.bind(itemView)
        fun bind(album: Album) {
            binding.tvAlbumId.text =
                if (onlySaved) (layoutPosition + 1).toString() else album.id.toString()
            binding.tvAlbumTitle.text = album.title
            binding.icManipulate.visibility = if (onlySaved) View.VISIBLE else View.GONE
            binding.layAlbum.setOnClickListener { open(albums?.get(layoutPosition)) }
            binding.icManipulate.setOnClickListener {
                remove?.run {
                    val albumId = albums?.get(layoutPosition)?.id
                    albumId?.let {
                        invoke(layoutPosition, it)
                        iconEnabled = false
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.holder_album, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int = albums?.size ?: 0

    override fun onBindViewHolder(holder: Holder, position: Int) {
        albums?.let { holder.bind(it[position]) }
    }

}
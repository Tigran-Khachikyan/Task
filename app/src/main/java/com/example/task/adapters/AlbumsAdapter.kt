package com.example.task.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.task.R
import com.example.task.databinding.HolderAlbumBinding
import com.example.task.model.Album

class AlbumsAdapter(
    private var albums: List<Album>?,
    private val onlySaved: Boolean,
    private val openInfo: (id: Int) -> Unit,
    private val saveOrRemove: (id: Int) -> Unit
) :
    RecyclerView.Adapter<AlbumsAdapter.Holder>() {

    fun setAlbums(albums: List<Album>?) {
        this.albums = albums
        notifyDataSetChanged()
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = HolderAlbumBinding.bind(itemView)
        fun bind(album: Album) {
            binding.album = album
            val drawResIcon: Int =
                if (onlySaved) R.drawable.ic_trash else {
                    if (album.saved) R.drawable.ic_star_favourite
                    else R.drawable.ic_star
                }
            binding.icManipulate.setImageDrawable(
                ResourcesCompat.getDrawable(itemView.context.resources, drawResIcon, null)
            )
            binding.layAlbum.setOnClickListener { openInfo(layoutPosition) }
            binding.icManipulate.setOnClickListener { saveOrRemove(layoutPosition) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.holder_album, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int = albums?.size ?: 0

    override fun onBindViewHolder(holder: Holder, position: Int) {
        albums?.let {
            holder.bind(it[position])
        }
    }

}
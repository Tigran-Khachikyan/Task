package com.example.task.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.task.R
import com.example.task.databinding.HolderInfoBinding
import com.example.task.model.Album
import com.example.task.model.Info

class InfoAdapter(
    private var info: List<Info>?,
    private val openPhoto: (Int) -> Unit
) :
    RecyclerView.Adapter<InfoAdapter.Holder>() {

    fun setAlbums(info: List<Info>?) {
        this.info = info
        notifyDataSetChanged()
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = HolderInfoBinding.bind(itemView)
        fun bind(info: Info) {
            binding.tvInfoId.text = (layoutPosition+1).toString()
            binding.tvInfoTitle.text = info.title
            Log.d("jahsdkasd","thumbnailUrl: ${info.thumbnailUrl}")
            Log.d("jahsdkasd","url: ${info.url}")
            Glide.with(itemView.context)
                .load(info.thumbnailUrl)
              //  .centerCrop()
                .placeholder(R.drawable.ic_image_placeholder)
                .dontAnimate()
                .into(binding.ivPhotoInto)
            binding.ivPhotoInto.setOnClickListener { openPhoto(layoutPosition) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.holder_info, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int = info?.size ?: 0

    override fun onBindViewHolder(holder: Holder, position: Int) {
        info?.let { holder.bind(it[position]) }
    }

}
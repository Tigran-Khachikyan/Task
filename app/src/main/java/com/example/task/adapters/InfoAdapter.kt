package com.example.task.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.task.R
import com.example.task.databinding.HolderInfoBinding
import com.example.task.model.Info
import com.squareup.picasso.Picasso

class InfoAdapter(
    private var infoList: List<Info>?,
    private val openPhoto: (Info?) -> Unit
) :
    RecyclerView.Adapter<InfoAdapter.Holder>() {

    fun setAlbums(info: List<Info>?) {
        this.infoList = info
        notifyDataSetChanged()
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = HolderInfoBinding.bind(itemView)
        fun bind(info: Info) {
            binding.tvInfoId.text = (layoutPosition + 1).toString()
            binding.tvInfoTitle.text = info.title
            Picasso.get()
                .load(info.thumbnailUrl)
                .placeholder(R.drawable.ic_image_placeholder)
                .into(binding.ivPhotoInto)

            binding.ivPhotoInto.setOnClickListener { openPhoto(infoList?.get(layoutPosition)) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.holder_info, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int = infoList?.size ?: 0

    override fun onBindViewHolder(holder: Holder, position: Int) {
        infoList?.let { holder.bind(it[position]) }
    }

}
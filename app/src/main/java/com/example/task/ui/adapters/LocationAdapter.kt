package com.example.task.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.task.R
import com.example.task.databinding.HolderLocationBinding
import com.example.task.model.LocationInfo

class LocationAdapter : RecyclerView.Adapter<LocationAdapter.Holder>() {

    private var locationInfo: List<Pair<String, String>>? = null

    fun updateLocation(infoList: List<Pair<String, String>>?) {
        locationInfo = infoList
        notifyDataSetChanged()
    }


    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = HolderLocationBinding.bind(itemView)

        fun bind(info: Pair<String, String>) {

            binding.tvLocSubInfo.text = info.first
            binding.tvLocInfo.text = info.second
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.holder_location, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int = locationInfo?.size ?: 0

    override fun onBindViewHolder(holder: Holder, position: Int) {
        locationInfo?.let { holder.bind(it[position]) }
    }

}
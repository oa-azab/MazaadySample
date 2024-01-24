package com.mazaady.task.secondscreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mazaady.task.databinding.ItemLiveBinding

class LiveAdapter(private val imgIds: List<Int>) : RecyclerView.Adapter<LiveAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binds = ItemLiveBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binds)
    }

    override fun getItemCount(): Int {
        return imgIds.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(imgIds[position])
    }

    class ViewHolder(private val binds: ItemLiveBinding) : RecyclerView.ViewHolder(binds.root) {

        fun bind(imgResId: Int) {
            binds.img.setImageResource(imgResId)
        }
    }
}
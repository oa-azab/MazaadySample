package com.mazaady.task.ui.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mazaady.task.databinding.ItemOptionBinding
import com.mazaady.task.model.Option

class OptionAdapter(
    private val options: List<Option>,
    private val onClick: (Option) -> Unit
) : RecyclerView.Adapter<OptionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binds = ItemOptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binds)
    }

    override fun getItemCount(): Int {
        return options.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(options[position]) { onClick(it) }
    }

    class ViewHolder(private val binds: ItemOptionBinding) : RecyclerView.ViewHolder(binds.root) {

        fun bind(option: Option, onClick: (Option) -> Unit) {
            binds.root.setOnClickListener { onClick(option) }
            binds.tvOptionName.text = option.name
        }

    }


}
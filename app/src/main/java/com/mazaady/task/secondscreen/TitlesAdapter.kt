package com.mazaady.task.secondscreen

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mazaady.task.R
import com.mazaady.task.databinding.ItemTitleBinding

class TitlesAdapter(
    private val titles: List<String>,
    private var selectedItemPosition: Int = 0
) : RecyclerView.Adapter<TitlesAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binds = ItemTitleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binds)
    }

    override fun getItemCount(): Int {
        return titles.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(titles[position], selectedItemPosition == position) { selectionPosition ->
            val oldPosition = selectedItemPosition
            selectedItemPosition = selectionPosition
            notifyItemChanged(oldPosition)
            notifyItemChanged(selectionPosition)
        }
    }

    class ViewHolder(private val binds: ItemTitleBinding) : RecyclerView.ViewHolder(binds.root) {

        fun bind(title: String, selected: Boolean, onClick: (Int) -> Unit) {
            binds.root.setOnClickListener { onClick(adapterPosition) }
            binds.tvTitles.text = title
            if (selected) {
                binds.tvTitles.setBackgroundResource(R.drawable.bg_title_selected)
                binds.tvTitles.setTextColor(Color.WHITE)
            } else {
                binds.tvTitles.setBackgroundResource(R.drawable.bg_title_not_selected)
                binds.tvTitles.setTextColor(Color.GRAY)
            }
        }

    }

}
package com.mazaady.task.ui.result

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mazaady.task.databinding.ItemResultBinding
import com.mazaady.task.model.AppResult

class ResultAdapter(
    private val list: List<AppResult>
) : RecyclerView.Adapter<ResultAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binds = ItemResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binds)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    class ViewHolder(private val binds: ItemResultBinding) : RecyclerView.ViewHolder(binds.root) {

        fun bind(result: AppResult) {
            binds.tvKey.text = result.key
            binds.tvValue.text = result.value
        }

    }

}
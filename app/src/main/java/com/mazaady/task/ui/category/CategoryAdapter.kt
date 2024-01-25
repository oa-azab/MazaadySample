package com.mazaady.task.ui.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mazaady.task.databinding.ItemCategoryBinding
import com.mazaady.task.model.Category

class CategoryAdapter(
    private val categories: List<Category>,
    private val onClick: (Category) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binds = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binds)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(categories[position]) { onClick(it) }
    }

    class ViewHolder(private val binds: ItemCategoryBinding) : RecyclerView.ViewHolder(binds.root) {

        fun bind(category: Category, onClick: (Category) -> Unit) {
            binds.root.setOnClickListener { onClick(category) }
            binds.tvCategoryName.text = category.name
        }

    }


}
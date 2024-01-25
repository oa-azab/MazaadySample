package com.mazaady.task.ui.categoryscreen

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mazaady.task.databinding.ItemPropertyBinding
import com.mazaady.task.model.Property

class PropertyListAdapter(
    private val onClick: (Property) -> Unit
) : ListAdapter<Property, PropertyListAdapter.ViewHolder>(diffUtil) {

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Property>() {
            override fun areItemsTheSame(oldItem: Property, newItem: Property): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Property, newItem: Property): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binds = ItemPropertyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binds)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onClick)
    }

    class ViewHolder(private val binds: ItemPropertyBinding) : RecyclerView.ViewHolder(binds.root) {

        fun bind(property: Property, onClick: (Property) -> Unit) {
            binds.inputProperty.hint = property.name
            binds.edtProperty.setText(property.selectedOption?.name)

            binds.edtProperty.setOnClickListener {
                Log.d("PropertyAdapter", "input Clicked!")
                onClick(property)
            }

            val userInput = property.selectedOption?.userInput
            if (userInput != null) {
                binds.inputCustom.visibility = View.VISIBLE
                binds.edtCustom.setText(userInput)
            } else {
                binds.inputCustom.visibility = View.GONE
                binds.edtCustom.text = null
            }
        }

    }

}
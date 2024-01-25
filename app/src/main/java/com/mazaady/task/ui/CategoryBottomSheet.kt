package com.mazaady.task.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mazaady.task.databinding.CategoryBottomSheetBinding
import com.mazaady.task.model.Category
import com.mazaady.task.ui.category.CategoryAdapter

class CategoryBottomSheet(
    private val categories: List<Category>,
    private val onClick: (Category) -> Unit
) : BottomSheetDialogFragment() {

    private var _binds: CategoryBottomSheetBinding? = null
    private val binds: CategoryBottomSheetBinding
        get() = _binds!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binds = CategoryBottomSheetBinding.inflate(inflater, container, false)
        return binds.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binds.rvCategory.layoutManager = LinearLayoutManager(context)
        binds.rvCategory.adapter = CategoryAdapter(categories) {
            onClick(it)
            dismiss()
        }
    }

    override fun onDestroyView() {
        _binds = null
        super.onDestroyView()
    }

    companion object {
        const val TAG = "CategoryBottomSheet"
    }
}
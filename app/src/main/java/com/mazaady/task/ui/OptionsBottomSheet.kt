package com.mazaady.task.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mazaady.task.databinding.OptionsBottomSheetBinding
import com.mazaady.task.model.Option
import com.mazaady.task.ui.categoryscreen.OptionAdapter

class OptionsBottomSheet(
    private val options: List<Option>,
    private val onClick: (Option) -> Unit
) : BottomSheetDialogFragment() {

    private var _binds: OptionsBottomSheetBinding? = null
    private val binds: OptionsBottomSheetBinding
        get() = _binds!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binds = OptionsBottomSheetBinding.inflate(inflater, container, false)
        return binds.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binds.rvOptions.layoutManager = LinearLayoutManager(context)
        binds.rvOptions.adapter = OptionAdapter(options) {
            onClick(it)
            dismiss()
        }
    }

    override fun onDestroyView() {
        _binds = null
        super.onDestroyView()
    }

    companion object {
        const val TAG = "OptionsBottomSheet"
    }
}
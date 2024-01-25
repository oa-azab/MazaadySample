package com.mazaady.task.ui.category

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.mazaady.task.databinding.ActivityCategoryBinding
import com.mazaady.task.databinding.ItemCustomOptionDialogBinding
import com.mazaady.task.model.Category
import com.mazaady.task.model.Option
import com.mazaady.task.model.Option.Companion.NOT_LISTED_OPTION
import com.mazaady.task.model.Property
import com.mazaady.task.ui.CategoryBottomSheet
import com.mazaady.task.ui.OptionsBottomSheet
import com.mazaady.task.ui.result.ResultActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryActivity : AppCompatActivity() {

    private lateinit var binds: ActivityCategoryBinding
    private val viewModel: CategoryViewModel by viewModels()
    private val propertyAdapter = PropertyListAdapter { property ->
        openOptionsBottomSheet(property.options) { clickedOption ->
            if (clickedOption.id == NOT_LISTED_OPTION.id) {
                // ask user for input first
                askUserForCustomOptionInput(property)
            } else {
                viewModel.propOptionSelected(property, clickedOption)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binds = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binds.root)

        binds.rvProps.layoutManager = LinearLayoutManager(this)
        binds.rvProps.adapter = propertyAdapter

        binds.btnSubmit.setOnClickListener {
            val result = viewModel.getSelectedOptions()
            ResultActivity.start(this, result)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->

                    when (uiState) {
                        is CategoryViewModel.UiState.Data -> {
                            binds.progress.visibility = View.GONE
                            val selectedCategory = uiState.selectedCategory
                            if (selectedCategory != null) {
                                binds.edtCategory.setText(selectedCategory.name)

                                val children = selectedCategory.children
                                if (children.isNotEmpty()) {
                                    binds.edtSubCategory.setOnClickListener {
                                        openCategoryBottomSheet(children) {
                                            viewModel.selectSubCategory(it)
                                        }
                                    }
                                } else {
                                    binds.edtSubCategory.setText("No SubCategory")
                                }
                            }

                            val selectedSubCategory = uiState.selectedSubCategory
                            if (selectedSubCategory != null) {
                                binds.edtSubCategory.setText(selectedSubCategory.name)
                            } else {
                                binds.edtSubCategory.text = null
                            }

                            binds.edtCategory.setOnClickListener {
                                openCategoryBottomSheet(uiState.categories) {
                                    viewModel.selectCategory(it)
                                }
                            }

                            // Show properties
                            val props = uiState.props.orEmpty()
                            showProperties(props)
                        }

                        is CategoryViewModel.UiState.Loading -> {
                            binds.progress.visibility = View.VISIBLE
                        }

                        is CategoryViewModel.UiState.Error -> {
                            binds.progress.visibility = View.GONE
                            Toast.makeText(
                                this@CategoryActivity,
                                uiState.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }

        viewModel.getCategories()
    }

    private fun showProperties(props: List<Property>) {
        propertyAdapter.submitList(props)
    }

    private fun openCategoryBottomSheet(categories: List<Category>, onClick: (Category) -> Unit) {
        val modal = CategoryBottomSheet(categories, onClick)
        modal.show(supportFragmentManager, CategoryBottomSheet.TAG)
    }

    private fun openOptionsBottomSheet(options: List<Option>, onClick: (Option) -> Unit) {
        val modal = OptionsBottomSheet(options, onClick)
        modal.show(supportFragmentManager, OptionsBottomSheet.TAG)
    }

    private fun askUserForCustomOptionInput(property: Property) {
        val binds = ItemCustomOptionDialogBinding.inflate(layoutInflater, null, false)
        val dialog = AlertDialog.Builder(this)
            .setView(binds.root)
            .setPositiveButton("Done") { _, _ ->
                val str = binds.edtCustomOption.text.toString()
                val option = NOT_LISTED_OPTION.copy(userInput = str)
                viewModel.propOptionSelected(property, option)
            }
            .create()
        dialog.show()
    }

}
package com.mazaady.task.ui.categoryscreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mazaady.task.data.RemoteCategoryDataSource
import com.mazaady.task.model.AppResult
import com.mazaady.task.model.Category
import com.mazaady.task.model.Option
import com.mazaady.task.model.Property
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val dataSource: RemoteCategoryDataSource
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState(emptyList()))
    val uiState: StateFlow<UiState> = _uiState

    fun getCategories() {
        viewModelScope.launch {
            val list = dataSource.getAllCategories()
            list.forEach {
                Log.d("CategoryViewModel", it.toString())
            }
            _uiState.value = UiState(list)
        }
    }

    fun selectCategory(category: Category) {
        val newState = _uiState.value.copy(
            selectedCategory = category,
            selectedSubCategory = null,
            props = null
        )
        _uiState.value = newState
        // TODO: If has no children
    }

    fun selectSubCategory(category: Category) {
        val newState = _uiState.value.copy(
            selectedSubCategory = category,
            props = null
        )
        _uiState.value = newState
        getProps(category.id)
    }

    private fun getProps(id: String) {
        viewModelScope.launch {
            val props = dataSource.getPropertiesOfCategory(id)
            val newState = _uiState.value.copy(props = props)
            _uiState.value = newState
        }
    }

    fun propOptionSelected(property: Property, option: Option) {
        viewModelScope.launch {
            // Update current state
            val currentProps = _uiState.value.props ?: return@launch
            val optionChildren = if (option.hasChildren) {
                dataSource.getPropertyWithOptionId(option.id)
            } else {
                emptyList()
            }

            // TODO: remove old
            // Remove children of property
            // val listOfParentIdsToRemove = mutableListOf<String>()
            // val toDelete = currentProps.find { it.id == property.id }?.selectedOption?.id
            // if (toDelete != null) {
            //     val searchFor = mutableListOf(toDelete)
            //     while (searchFor.isNotEmpty()) {
            //         val ids = currentProps.filter { searchFor.contains(it.parentId) }
            //             .map { it.id }
            //         listOfParentIdsToRemove += ids
            //         searchFor.clear()
            //         searchFor += ids
            //     }
            // }

            // val filteredProps = currentProps.filterNot { listOfParentIdsToRemove.contains(it.id) }

            val newProps = mutableListOf<Property>()
            for (prop in currentProps) {
                if (prop.id == property.id) {
                    val newProp = prop.copy(selectedOption = option)
                    newProps += newProp
                    newProps += optionChildren
                } else {
                    newProps += prop
                }
            }
            val newState = _uiState.value.copy(props = newProps.toList())
            _uiState.value = newState
        }
    }

    fun getSelectedOptions(): ArrayList<AppResult> {
        val result = arrayListOf(AppResult("Key", "Value"))

        val categoryKey = "Category"
        val categoryValue = uiState.value.selectedCategory?.name.orEmpty()
        result += AppResult(categoryKey, categoryValue)

        val subCategoryKey = "SubCategory"
        val subCategoryValue = uiState.value.selectedSubCategory?.name.orEmpty()
        result += AppResult(subCategoryKey, subCategoryValue)

        val props = uiState.value.props ?: emptyList()
        val options = props.filter { it.selectedOption != null }
            .map { it.name to it.selectedOption!! }

        options.forEach { (propName, option) ->
            result += AppResult(propName, option.getNameValue())
        }

        Log.d("getSelectedOptions", "Print all")
        result.forEach {
            Log.d("getSelectedOptions", it.toString())
        }

        return result
    }

    data class UiState(
        val categories: List<Category>,
        val selectedCategory: Category? = null,
        val selectedSubCategory: Category? = null,
        val props: List<Property>? = null
    )
}
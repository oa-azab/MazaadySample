package com.mazaady.task.ui.category

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mazaady.task.domain.GetCategoriesUseCase
import com.mazaady.task.domain.GetPropertiesOfCategoryUseCase
import com.mazaady.task.domain.SelectPropertyOptionUseCase
import com.mazaady.task.model.AppResult
import com.mazaady.task.model.Category
import com.mazaady.task.model.Option
import com.mazaady.task.model.Property
import com.mazaady.task.model.UCResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getPropertiesOfCategoryUseCase: GetPropertiesOfCategoryUseCase,
    private val selectPropertyOptionUseCase: SelectPropertyOptionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState(emptyList()))
    val uiState: StateFlow<UiState> = _uiState

    fun getCategories() {
        viewModelScope.launch {
            when (val result = getCategoriesUseCase.invoke()) {
                is UCResult.Success -> {
                    _uiState.value = UiState(result.data)
                }

                is UCResult.Error -> {
                    Log.w("getCategories", result.throwable)
                    // TODO:
                }
            }
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
            val result = getPropertiesOfCategoryUseCase.invoke(id)
            when (result) {
                is UCResult.Success -> {
                    val newState = _uiState.value.copy(props = result.data)
                    _uiState.value = newState
                }

                is UCResult.Error -> {
                    Log.w("getCategories", result.throwable)
                    // TODO:
                }
            }

        }
    }

    fun propOptionSelected(property: Property, option: Option) {
        viewModelScope.launch {
            val currentProps = _uiState.value.props ?: return@launch

            val result = selectPropertyOptionUseCase.invoke(currentProps, property, option)
            when (result) {
                is UCResult.Success -> {
                    val newState = _uiState.value.copy(props = result.data)
                    _uiState.value = newState
                }

                is UCResult.Error -> {
                    Log.w("propOptionSelected", result.throwable)
                    // TODO
                }
            }

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
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

    private var dataModel = UiState.Data(emptyList())

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    fun getCategories() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            when (val result = getCategoriesUseCase.invoke()) {
                is UCResult.Success -> {
                    dataModel = UiState.Data(result.data)
                    _uiState.value = dataModel
                }

                is UCResult.Error -> {
                    Log.w("getCategories", result.throwable)
                    _uiState.value = UiState.Error(result.throwable.localizedMessage)
                }
            }
        }
    }

    fun selectCategory(category: Category) {
        val newState = dataModel.copy(
            selectedCategory = category,
            selectedSubCategory = null,
            props = null
        )
        dataModel = newState
        _uiState.value = newState
        // TODO: If has no children
    }

    fun selectSubCategory(category: Category) {
        val newState = dataModel.copy(
            selectedSubCategory = category,
            props = null
        )
        dataModel = newState
        _uiState.value = newState
        getProps(category.id)
    }

    private fun getProps(id: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val result = getPropertiesOfCategoryUseCase.invoke(id)
            when (result) {
                is UCResult.Success -> {
                    val newState = dataModel.copy(props = result.data)
                    dataModel = newState
                    _uiState.value = newState
                }

                is UCResult.Error -> {
                    Log.w("getCategories", result.throwable)
                    _uiState.value = UiState.Error(result.throwable.localizedMessage)
                }
            }

        }
    }

    fun propOptionSelected(property: Property, option: Option) {
        viewModelScope.launch {
            val currentProps = dataModel.props ?: return@launch

            val result = selectPropertyOptionUseCase.invoke(currentProps, property, option)
            when (result) {
                is UCResult.Success -> {
                    val newState = dataModel.copy(props = result.data)
                    dataModel = newState
                    _uiState.value = newState
                }

                is UCResult.Error -> {
                    Log.w("propOptionSelected", result.throwable)
                    _uiState.value = UiState.Error(result.throwable.localizedMessage)
                }
            }

        }
    }

    fun getSelectedOptions(): ArrayList<AppResult> {
        val result = arrayListOf(AppResult("Key", "Value"))

        val categoryKey = "Category"
        val categoryValue = dataModel.selectedCategory?.name.orEmpty()
        result += AppResult(categoryKey, categoryValue)

        val subCategoryKey = "SubCategory"
        val subCategoryValue = dataModel.selectedSubCategory?.name.orEmpty()
        result += AppResult(subCategoryKey, subCategoryValue)

        val props = dataModel.props ?: emptyList()
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

    sealed class UiState {
        data class Data(
            val categories: List<Category>,
            val selectedCategory: Category? = null,
            val selectedSubCategory: Category? = null,
            val props: List<Property>? = null
        ) : UiState()

        data object Loading : UiState()
        data class Error(val message: String) : UiState()
    }

}
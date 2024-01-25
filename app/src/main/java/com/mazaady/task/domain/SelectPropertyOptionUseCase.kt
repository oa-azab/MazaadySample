package com.mazaady.task.domain

import android.util.Log
import com.mazaady.task.data.RemoteCategoryDataSource
import com.mazaady.task.model.Option
import com.mazaady.task.model.Property
import com.mazaady.task.model.UCResult
import javax.inject.Inject

class SelectPropertyOptionUseCase @Inject constructor(
    private val dataSource: RemoteCategoryDataSource
) {

    suspend fun invoke(
        currentProperties: List<Property>,
        property: Property,
        option: Option
    ): UCResult<List<Property>> {
        val optionChildren = if (option.hasChildren) {
            getOptionChildrenOrEmpty(option.id)
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
        for (prop in currentProperties) {
            if (prop.id == property.id) {
                val newProp = prop.copy(selectedOption = option)
                newProps += newProp
                newProps += optionChildren
            } else {
                newProps += prop
            }
        }

        return UCResult.Success(newProps.toList())
    }

    private suspend fun getOptionChildrenOrEmpty(id: String): List<Property> {
        return try {
            dataSource.getPropertyWithOptionId(id)
        } catch (t: Throwable) {
            Log.w("SelectPropertyOptionUseCase", t)
            Log.w("SelectPropertyOptionUseCase", "Failed to get options, returning empty list")
            emptyList()
        }

    }

}
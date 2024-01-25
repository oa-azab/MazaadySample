package com.mazaady.task.data

import com.mazaady.task.model.Category
import com.mazaady.task.model.Option
import com.mazaady.task.model.Option.Companion.NOT_LISTED_OPTION
import com.mazaady.task.model.Property
import javax.inject.Inject

class RemoteCategoryDataSource @Inject constructor(
    private val service: CategoryService
) {

    suspend fun getAllCategories(): List<Category> {
        val response = service.getAllCategories()
        return if (response.isSuccessful) {
            parseResponse(response.body())
        } else {
            throw Exception(response.errorBody()?.string())
        }
    }

    suspend fun getPropertiesOfCategory(id: String): List<Property> {
        val response = service.getPropertiesOfCategory(id)
        return if (response.isSuccessful) {
            parsePropertiesResponse(response.body())
        } else {
            throw Exception(response.errorBody()?.string())
        }
    }

    suspend fun getPropertyWithOptionId(id: String): List<Property> {
        val response = service.getPropertiesWithOptionId(id)
        return if (response.isSuccessful) {
            parsePropertiesResponse(response.body())
        } else {
            throw Exception(response.errorBody()?.string())
        }
    }

    private fun parseResponse(body: RemoteCategoryResponse?): List<Category> {
        val remoteCategories = body?.data?.categories ?: emptyList()
        return remoteCategories.filterNotNull().map { parseRemoteCategory(it) }
    }

    private fun parseRemoteCategory(remote: RemoteCategory): Category {
        return Category(
            remote.id.orEmpty(),
            remote.name.orEmpty(),
            remote.children?.filterNotNull()?.map { parseRemoteCategory(it) } ?: emptyList()
        )
    }


    private fun parsePropertiesResponse(body: RemotePropertyResponse?): List<Property> {
        val props = body?.data?.filterNotNull() ?: emptyList()
        val result = mutableListOf<Property>()
        for (prop in props) {
            if (prop.id == null) continue
            result += Property(
                prop.id,
                prop.name.orEmpty(),
                prop.parentId,
                paresOptions(prop.options)
            )
        }
        return result.toList()
    }

    private fun paresOptions(options: List<RemoteOption?>?): List<Option> {
        if (options == null) return emptyList()
        val result = mutableListOf<Option>()
        for (item in options.filterNotNull()) {
            if (item.id == null) continue
            result += Option(
                item.id,
                item.name.orEmpty(),
                item.hasChildren ?: false
            )
        }
        result += NOT_LISTED_OPTION
        return result.toList()
    }
}
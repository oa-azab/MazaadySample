package com.mazaady.task.domain

import com.mazaady.task.data.RemoteCategoryDataSource
import com.mazaady.task.model.Property
import com.mazaady.task.model.UCResult
import javax.inject.Inject

class GetPropertiesOfCategoryUseCase @Inject constructor(
    private val dataSource: RemoteCategoryDataSource
) {

    suspend fun invoke(id: String): UCResult<List<Property>> {
        return try {
            UCResult.Success(dataSource.getPropertiesOfCategory(id))
        } catch (t: Throwable) {
            UCResult.Error(t)
        }
    }
}
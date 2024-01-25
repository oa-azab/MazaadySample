package com.mazaady.task.domain

import com.mazaady.task.data.RemoteCategoryDataSource
import com.mazaady.task.model.Category
import com.mazaady.task.model.UCResult
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val dataSource: RemoteCategoryDataSource
) {

    suspend fun invoke(): UCResult<List<Category>> {
        return try {
            UCResult.Success(dataSource.getAllCategories())
        } catch (t: Throwable) {
            UCResult.Error(t)
        }
    }
}
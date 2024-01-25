package com.mazaady.task.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface CategoryService {

    companion object {
        private const val keyHeader = "private-key: 3%o8i}_;3D4bF]G5@22r2)Et1&mLJ4?$@+16"
    }

    @GET("get_all_cats")
    @Headers(keyHeader)
    suspend fun getAllCategories(): Response<RemoteCategoryResponse?>


    @GET("properties")
    @Headers(keyHeader)
    suspend fun getPropertiesOfCategory(@Query("cat") categoryId: String): Response<RemotePropertyResponse?>

    @GET("get-options-child/{id}")
    @Headers(keyHeader)
    suspend fun getPropertiesWithOptionId(@Path("id") id: String): Response<RemotePropertyResponse?>

}
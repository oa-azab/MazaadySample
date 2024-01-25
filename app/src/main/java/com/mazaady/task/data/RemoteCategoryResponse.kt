package com.mazaady.task.data

import com.google.gson.annotations.SerializedName

data class RemoteCategoryResponse(
    @SerializedName("data")
    val data: RemoteCategoryData?
)

data class RemoteCategoryData(
    @SerializedName("categories")
    val categories: List<RemoteCategory?>?
)

data class RemoteCategory(
    @SerializedName("id")
    val id: String?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("children")
    val children: List<RemoteCategory?>?,
)
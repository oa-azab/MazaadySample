package com.mazaady.task.data

import com.google.gson.annotations.SerializedName

data class RemotePropertyResponse(
    @SerializedName("data")
    val data: List<RemoteProperty?>?
)

data class RemoteProperty(
    @SerializedName("id")
    val id: String?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("parent")
    val parentId: String?,

    @SerializedName("options")
    val options: List<RemoteOption?>?
)

data class RemoteOption(
    @SerializedName("id")
    val id: String?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("child")
    val hasChildren: Boolean?
)
package com.mazaady.task.model

data class Property(
    val id: String,
    val name: String,
    val parentId: String?,
    val options: List<Option>,
    val selectedOption: Option? = null
)
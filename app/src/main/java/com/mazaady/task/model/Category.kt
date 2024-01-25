package com.mazaady.task.model

data class Category(
    val id: String,
    val name: String,
    val children: List<Category>
)
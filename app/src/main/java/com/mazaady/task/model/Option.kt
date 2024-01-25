package com.mazaady.task.model

data class Option(
    val id: String,
    val name: String,
    val hasChildren: Boolean,
    val userInput: String? = null
) {
    companion object {
        val NOT_LISTED_OPTION = Option("-1", "Other", false)
    }

    fun getNameValue(): String {
        return if (id == NOT_LISTED_OPTION.id)
            userInput.orEmpty()
        else
            name
    }
}
package com.mazaady.task.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AppResult(
    val key: String,
    val value: String
) : Parcelable
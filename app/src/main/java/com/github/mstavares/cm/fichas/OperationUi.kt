package com.github.mstavares.cm.fichas

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OperationUi(
    val uuid: String, val expression: String, val result: Double, val timestamp: Long
) : Parcelable

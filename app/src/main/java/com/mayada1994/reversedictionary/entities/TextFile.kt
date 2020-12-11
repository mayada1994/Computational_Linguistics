package com.mayada1994.reversedictionary.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TextFile(
    val filename: String?,
    val text: String
) : Parcelable
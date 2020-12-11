package com.mayada1994.reversedictionary.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ZipfWord(
    val id: Int,
    var word: String,
    val frequency: Int,
    var rank: Int = 1
) : Parcelable
package com.mayada1994.zipfslaw.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ZipfWord(
    val id: Int,
    val word: String,
    val frequency: Int,
    var rank: Int = 1
) : Parcelable
package com.mayada1994.zipfslaw.entities

data class ZipfWord(
    val id: Int,
    val word: String,
    val frequency: Int,
    var rank: Int = 1
)
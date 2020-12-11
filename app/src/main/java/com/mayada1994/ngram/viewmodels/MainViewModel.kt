package com.mayada1994.ngram.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class MainViewModel(application: Application) : AndroidViewModel(application) {

    fun getRelevance(firstStr: String, secondStr: String, lengthOfGram: Int): Pair<String, String> {
        val firstString: String = firstStr.toLowerCase().replace("[^а-яa-z0-9]", "")
        val secondString: String = secondStr.toLowerCase().replace("[^а-яa-z0-9]", "")
        val nGramMatch1 = getNGramMatch(firstString, secondString, lengthOfGram)
        val nGramMatch2 = getNGramMatch(secondString, firstString, lengthOfGram)
        val nGramCount1 = getNGramCount(firstString, lengthOfGram)
        val nGramCount2 = getNGramCount(secondString, lengthOfGram)
        val result1 = (nGramMatch1.toDouble() / nGramCount2 * 100).toString()
        val result2 = (nGramMatch2.toDouble() / nGramCount1 * 100).toString()
        return result1 to result2
    }

    private fun getNGramMatch(str1: String, str2: String, lengthOfGram: Int): Int {
        val str1CharsArray = str1.toCharArray()
        val str2CharsArray = str2.toCharArray()
        val partOfStr1 = getPartOfStr(str1, str1CharsArray, lengthOfGram)
        val partOfStr2 = getPartOfStr(str2, str2CharsArray, lengthOfGram)
        var countOfMatch = 0
        for (part in partOfStr1) {
            if (partOfStr2.contains(part)) {
                countOfMatch++
            }
        }
        return countOfMatch
    }

    private fun getPartOfStr(
        str: String,
        strCharsArray: CharArray,
        lengthOfGram: Int
    ): List<String> {
        val result: MutableList<String> = ArrayList()
        var temp = 0
        var nextChar = lengthOfGram
        for (i in strCharsArray.indices) {
            if (nextChar <= strCharsArray.size) {
                result.add(str.substring(temp, nextChar))
                temp++
                nextChar++
            }
        }
        return result
    }

    private fun getNGramCount(str: String, lengthOfGram: Int): Int {
        return str.length - lengthOfGram + 1
    }

}
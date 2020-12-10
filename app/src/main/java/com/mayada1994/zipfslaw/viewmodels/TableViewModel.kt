package com.mayada1994.zipfslaw.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.mayada1994.zipfslaw.entities.ZipfWord

class TableViewModel(application: Application) : AndroidViewModel(application) {

    fun analyzeText(text: String): List<ZipfWord> {
        val words = parseText(text)
        val zipfWords = arrayListOf<ZipfWord>()
        words.distinct().forEachIndexed { index, word ->
            zipfWords.add(
                ZipfWord(
                    id = index,
                    word = word,
                    words.count { it == word }
                )
            )
        }
        return calculateRank(zipfWords)
    }

    private fun calculateRank(zipfWords: List<ZipfWord>): List<ZipfWord> {
        val finalWords = arrayListOf<ZipfWord>()
        var lastBiggestFrequency = 0
        var lastRank = 1
        zipfWords.sortedByDescending { it.frequency }.forEach {
            when {
                it.frequency > lastBiggestFrequency -> {
                    lastBiggestFrequency = it.frequency
                }
                it.frequency == lastBiggestFrequency -> {
                }
                else -> {
                    lastBiggestFrequency = it.frequency
                    lastRank++
                }
            }
            it.rank = lastRank
            finalWords.add(it)
        }
        return finalWords
    }

    private fun parseText(textContent: String): List<String> {
        val words: MutableList<String> = ArrayList()
        val chars = textContent.toCharArray()
        val arrayOfStrings = convertCharArrayToStringArray(chars)
        val endOfTheWords: Set<String> = getWordEndings()
        val word = StringBuilder()
        for (i in arrayOfStrings.indices) {
            if (isSymbolIsLetter(arrayOfStrings, i)) {
                word.append(arrayOfStrings[i])
            } else if (isWordNotBlank(word)) {
                setWordToList(words, word, endOfTheWords)
            }
            if (isLastLetterInWord(arrayOfStrings, i)) {
                setWordToList(words, word, endOfTheWords)
            }
        }
        return additionalClearEmptyStringSymbol(words)
    }

    private fun getWordEndings() = setOf(
        "а",
        "и",
        "і",
        "у",
        "ою",
        "о",
        "я",
        "ю",
        "ею",
        "е",
        "ї",
        "єю",
        "є",
        "ам",
        "ами",
        "ах",
        "ям",
        "ями",
        "ях",
        "ові",
        "ом",
        "ів",
        "єві",
        "ем",
        "еві",
        "ей",
        "аті",
        "ені",
        "енем",
        "ата",
        "ат",
        "атам",
        "атами",
        "атах",
        "ена",
        "ен",
        "енам",
        "енами",
        "енах",
        "ері",
        "ір",
        "ір'ю",
        "ерів",
        "ерями",
        "ерям",
        "ерях",
        "ий",
        "ого",
        "ому",
        "им",
        "ім",
        "ої",
        "ій",
        "їй",
        "их",
        "ими",
        "іми",
        "їми",
        "їх",
        "еш",
        "єш",
        "емо",
        "ете",
        "уть",
        "ють",
        "ємо",
        "єте",
        "иш",
        "ить",
        "їш",
        "ите",
        "їте",
        "їть",
        "ять",
        "ать",
        "яти",
        "ати",
        "аю",
        "ію",
        "ають"
    )

    private fun additionalClearEmptyStringSymbol(list: List<String>): List<String> {
        return list.filter { it.isNotBlank() }
    }

    private fun convertCharArrayToStringArray(array: CharArray): Array<String?> {
        val result = arrayOfNulls<String>(array.size)
        for (i in array.indices) {
            result[i] = array[i].toString().toLowerCase()
        }
        return result
    }

    private fun isSymbolIsLetter(arrayOfStrings: Array<String?>, iteration: Int): Boolean {
        return arrayOfStrings[iteration]!!.matches("[a-zA-Zа-яА-Яії]+".toRegex())
    }

    private fun setWordToList(
        result: MutableList<String>,
        word: StringBuilder,
        endOfTheWords: Set<String>
    ) {
        if (word.length <= 3) {
            result.add(word.toString())
            word.setLength(0)
        } else {
            val wordWithoutEnding = excludeEndingInWord(word, endOfTheWords)
            result.add(wordWithoutEnding)
            word.setLength(0)
        }
    }

    private fun excludeEndingInWord(word: StringBuilder, endOfTheWords: Set<String>): String {
        val currentWord = word.toString()
        for (end in endOfTheWords) {
            if (currentWord.endsWith(end)) {
                return currentWord.substring(0, currentWord.indexOf(end))
            }
        }
        return currentWord
    }

    private fun isWordNotBlank(word: StringBuilder): Boolean {
        return word.toString().isNotBlank()
    }

    private fun isLastLetterInWord(arrayOfStrings: Array<String?>, iteration: Int): Boolean {
        return iteration == arrayOfStrings.size - 1
    }

}
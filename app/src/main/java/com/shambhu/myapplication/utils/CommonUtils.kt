package com.shambhu.myapplication.utils

import android.content.Context
import android.graphics.Color
import com.shambhu.myapplication.utils.Constants.Companion.LETTER_VALUES
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.Locale
import kotlin.toString

object CommonUtils {
    // Helper function to parse date
    fun parseDate(dateString: String): LocalDate {
        return try {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            sdf.parse(dateString)?.let { sdf.format(it) } ?: "Invalid Date"
            val sdfParse = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            sdfParse.parse(dateString)?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate()
                ?: LocalDate.of(0, 1, 1)
        } catch (_: Exception) {
            LocalDate.of(0, 1, 1)
        }
    }


    fun parseDateTriple(dateString: String): Triple<Int, Int, Int> {
        val d: LocalDate = try {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            sdf.parse(dateString)?.let { sdf.format(it) } ?: "Invalid Date"
            val sdfParse = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            sdfParse.parse(dateString)?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate()
                ?: LocalDate.of(0, 1, 1)
        } catch (_: Exception) {
            LocalDate.of(0, 1, 1)
        }

        return Triple(d.dayOfMonth, d.monthValue, d.year)
    }

    fun sumDigits(number: Int): Int {
        var n = Math.abs(number)
        var sum = 0
        while (n > 0) {
            sum += n % 10
            n /= 10
        }
        return sum
    }

    fun reduceNumber(numerologyNumber: Int, ignore33: Boolean = true): Int {
        /** Reduce number to single digit unless it's a master number. */
        if (ignore33 && (numerologyNumber == 11 || numerologyNumber == 22)) {
            return numerologyNumber
        }
        if (numerologyNumber == 11 || numerologyNumber == 22 || numerologyNumber == 33) {
            return numerologyNumber
        }
        var number = numerologyNumber
        while (number > 9) {
            number = sumDigits(number)
        }
        return number
    }

    fun reduceNumberIgnoreMasterNumber(n: Int): Int {
        var number = n
        while (number > 9) {
            number = sumDigits(number)
        }
        return number
    }

    fun readAssetFile(context: Context, filename: String): String {
        return context.assets.open(filename).bufferedReader().use { it.readText() }
    }

    fun getDescriptionFromAssetFile(context: Context, filename: String, key: String): String {
        val elementsJson = readAssetFile(context, filename)
        val jsonObject = org.json.JSONObject(elementsJson)
        return NumerologyCalculationUtils.convertToHtml(jsonObject.getString(key))
    }

    fun nameToIntArray(name: String): IntArray {
        return name.uppercase()
            .mapNotNull { LETTER_VALUES[it] } // skip characters not in map
            .toIntArray()
    }

    fun getStateColor(state: String): Int {
        return when (state) {
            "universal benefic" -> Color.parseColor("#4CAF50")
            "neutral combinations" -> Color.parseColor("#FF9800")
            "malefic combinations" -> Color.parseColor("#F44336")
            else -> Color.parseColor("#9E9E9E")
        }
    }

    data class NumerologyResult(
        val lifePathNumber: NumerologyReducer.NumerologyValue,
        val attitude: NumerologyReducer.NumerologyValue,
        val birthDay: NumerologyReducer.NumerologyValue,
        val soul: NumerologyReducer.NumerologyValue,
        val personality: NumerologyReducer.NumerologyValue,
        val destiny: NumerologyReducer.NumerologyValue,
        val maturity: NumerologyReducer.NumerologyValue,
        val power: NumerologyReducer.NumerologyValue,
        val personalYear: NumerologyReducer.NumerologyValue
    )


    fun allMostFrequent(numbers: List<Int>): Map<Int, Int> {
        val freq = numbers.groupingBy { it }.eachCount()
        val maxCount = freq.values.max()
        return freq.filterValues { it == maxCount }
    }

    fun karmicOrMasterCount(list: List<String>): Map<String, Int> {
        return list
            .filter { it != NumerologyReducer.ResultType.OTHER.toString() }
            .groupingBy { it }
            .eachCount()

    }

    fun repeatedNumbersDesc(numbers: List<Int>): Map<Int, Int> {
        return numbers
            .groupingBy { it }
            .eachCount()
            .filterValues { it > 1 }
            .toSortedMap(compareByDescending { it })
    }
}
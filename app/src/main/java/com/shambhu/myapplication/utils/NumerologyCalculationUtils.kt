package com.shambhu.myapplication.utils

import com.shambhu.myapplication.utils.Constants.Companion.LETTER_VALUES
import java.time.LocalDate
import java.util.Locale
import kotlin.collections.component1
import kotlin.collections.component2

object NumerologyCalculationUtils {

    // Soul Urge (Heart's Desire) Number Calculation
    fun calculateSoulUrge(name: String): Int {
        // Convert name to all lowercase to handle vowels properly
        val cleanedName = name.replace(" ", "").lowercase(Locale.getDefault())

        // Count the vowels (a, e, i, o, u) and assign values
        var total = 0
        for (char in cleanedName) {
            val value = when (char) {
                'a' -> 1
                'e' -> 5
                'i' -> 9
                'o' -> 6
                'u' -> 3
                else -> 0 // For consonants
            }
            total += value
        }

        // Reduce to single digit or master numbers
        while (total > 9) {
            total = total.toString().map { it.toString().toInt() }.sum()
        }
        return total
    }

    fun getBirthdayNumber(day: Int): Int {
        return CommonUtils.reduceNumber(day)
    }

    // Life Path Number Calculation
    fun calculateLifePath(day: Int, month: Int, year: Int): Int {
        val reducedDay = CommonUtils.reduceNumber(day)
        val reducedMonth = CommonUtils.reduceNumber(month)
        val reducedYear = CommonUtils.reduceNumber(year)
        val sum = reducedDay + reducedMonth + reducedYear
        // For challenge age calculation, we need a single digit Life Path number.
        return CommonUtils.reduceNumber(sum)
    }

    // Expression (Destiny) Number Calculation
    fun calculateExpression(name: String): Int {
        // Convert name to all uppercase and remove spaces
        val cleanedName = name.replace(" ", "").uppercase(Locale.getDefault())

        // Assign numerology values to each letter
        var total = 0
        for (char in cleanedName) {
            val value = when (char) {
                'A', 'J', 'S' -> 1
                'B', 'K', 'T' -> 2
                'C', 'L', 'U' -> 3
                'D', 'M', 'V' -> 4
                'E', 'N', 'W' -> 5
                'F', 'O', 'X' -> 6
                'G', 'P', 'Y' -> 7
                'H', 'Q', 'Z' -> 8
                'I', 'R' -> 9
                else -> 0 // For non-alphabet characters
            }
            total += value
        }

        // Reduce to single digit or master numbers
        while (total > 9) {
            total = total.toString().map { it.toString().toInt() }.sum()
        }
        return total
    }

    // Personality Number Calculation
    fun calculatePersonality(name: String): Int {
        // Convert name to all uppercase and remove spaces
        val cleanedName = name.replace(" ", "").uppercase(Locale.getDefault())

        // Assign numerology values to each letter
        var total = 0
        for (char in cleanedName) {
            val value = when (char) {
                'B', 'K', 'T' -> 2
                'C', 'L', 'U' -> 3
                'D', 'M', 'V' -> 4
                'F', 'O', 'X' -> 6
                'G', 'P', 'Y' -> 7
                'H', 'Q', 'Z' -> 8
                else -> 0 // For non-alphabet characters
            }
            total += value
        }

        // Reduce to single digit or master numbers
        while (total > 9) {
            total = total.toString().map { it.toString().toInt() }.sum()
        }
        return total
    }

    fun calculatePersonalYear(day: Int, month: Int, year: Int = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)): Int {
        val birthSum = CommonUtils.reduceNumber(day) +  CommonUtils.reduceNumber(month)
        val yearSum = year.toString().map { it.toString().toInt() }.sum()
        return  CommonUtils.reduceNumber(birthSum + yearSum)
    }

    fun calculatePersonalMonth(day: Int, month: Int, targetMonth: Int = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH) + 1): Int {
        val currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
        val birthSum = CommonUtils.reduceNumber(day) + CommonUtils.reduceNumber(month)
        val yearSum = currentYear.toString().map { it.toString().toInt() }.sum()
        val personalYear = CommonUtils.reduceNumber(birthSum + yearSum)

        return CommonUtils.reduceNumber(personalYear + targetMonth)
    }

    fun calculateKarmicNumber(day: Int, month: Int, year: Int): String {
        fun digitSum(n: Int): Int = n.toString().map { it.toString().toInt() }.sum()

        val daySum = digitSum(day)
        val monthSum = digitSum(month)
        val yearSum = year.toString().map { it.toString().toInt() }.sum()

        val total = daySum + monthSum + yearSum
        val reduced = generateSequence(total) { digitSum(it) }
            .first { it < 10 }

        return when (total) {
            13, 14, 16, 19 -> "Karmic Debt Number: $total (Life Path: $reduced)"
            else -> "No karmic debt. Life Path Number: $reduced"
        }
    }

    fun calculateKarmicFromName(fullName: String): String {
        val cleanedName = fullName.uppercase().filter { it in LETTER_VALUES }
        val total = cleanedName.map { LETTER_VALUES[it] ?: 0 }.sum()

        // Reduce to single digit unless it's a karmic debt number
        fun reduce(n: Int): Int {
            var num = n
            while (num > 9 && num != 11 && num != 22) {
                num = num.toString().map { it.toString().toInt() }.sum()
            }
            return num
        }

        val karmicDebtNumbers = listOf(13, 14, 16, 19)
        return if (karmicDebtNumbers.contains(total)) {
            "Karmic Debt Number: $total (Core Number: ${reduce(total)})"
        } else {
            "No karmic debt. Core Number: ${reduce(total)}"
        }
    }
    fun calculateChallengeNumbers(day: Int, month: Int, year: Int): List<Int> {
        val reducedDay = CommonUtils.reduceNumber(day)
        val reducedMonth = CommonUtils.reduceNumber(month)
        val reducedYear = CommonUtils.reduceNumber(year)

        val firstChallenge = Math.abs(reducedDay - reducedMonth)
        val secondChallenge = Math.abs(reducedDay - reducedYear)
        val thirdChallenge = Math.abs(firstChallenge - secondChallenge)
        val fourthChallenge = Math.abs(reducedMonth - reducedYear)

        return listOf(firstChallenge, secondChallenge, thirdChallenge, fourthChallenge)
    }

    fun calculateChallengeNumberAgeRanges(day: Int, month: Int, year: Int): List<String> {
        val lifePathNumber = calculateLifePath(day, month, year)
        val endOfFirstChallenge = 36 - lifePathNumber
        val endOfSecondChallenge = endOfFirstChallenge + 9
        val endOfThirdChallenge = endOfSecondChallenge + 9

        return listOf(
            "Ages 0 - $endOfFirstChallenge",
            "Ages $endOfFirstChallenge - $endOfSecondChallenge",
            "Ages $endOfSecondChallenge - $endOfThirdChallenge",
            "Ages $endOfThirdChallenge onwards"
        )
    }

    fun calculatePinnacleNumbers(day: Int, month: Int, year: Int): List<Int> {
        val reducedDay = CommonUtils.reduceNumber(day)
        val reducedMonth = CommonUtils.reduceNumber(month)
        val reducedYear = CommonUtils.reduceNumber(year)

        val firstPinnacle = CommonUtils.reduceNumber(reducedMonth + reducedDay)
        val secondPinnacle = CommonUtils.reduceNumber(reducedDay + reducedYear)
        val thirdPinnacle = CommonUtils.reduceNumber(firstPinnacle + secondPinnacle)
        val fourthPinnacle = CommonUtils.reduceNumber(reducedMonth + reducedYear)

        return listOf(firstPinnacle, secondPinnacle, thirdPinnacle, fourthPinnacle)
    }

    fun calculatePinnacleNumberAgeRanges(day: Int, month: Int, year: Int): List<String> {
        val lifePathNumber = calculateLifePath(day, month, year)
        val endOfFirstPinnacle = 36 - lifePathNumber
        val endOfSecondPinnacle = endOfFirstPinnacle + 9
        val endOfThirdPinnacle = endOfSecondPinnacle + 9

        return listOf(
            "Ages 0 - $endOfFirstPinnacle",
            "Ages $endOfFirstPinnacle - $endOfSecondPinnacle",
            "Ages $endOfSecondPinnacle - $endOfThirdPinnacle",
            "Ages $endOfThirdPinnacle onwards"
        )
    }

    fun calculateLuckyNumber(day: Int): Int {
        return CommonUtils.reduceNumber(day)
    }

    fun calculateColorGroup(fullName: String, jsonString: String): Pair<String, String> {
        val nameNumbers = nameToColorNumbers(fullName)

        val jsonObject = org.json.JSONObject(jsonString)
        val colorByNumber = jsonObject.getJSONObject("color_by_number")
        val colorGroup = jsonObject.getJSONObject("color_group")

        val colorToGroupMap = mutableMapOf<String, String>()
        val groupIterator = colorGroup.keys()
        while (groupIterator.hasNext()) {
            val groupName = groupIterator.next()
            val groupObject = colorGroup.getJSONObject(groupName)
            val colorsInGroup = groupObject.getJSONArray("colors")
            for (i in 0 until colorsInGroup.length()) {
                val colorName = colorsInGroup.getString(i)
                colorToGroupMap[colorName] = groupName
            }
        }

        val groupCounts = nameNumbers
            .mapNotNull { colorByNumber.optJSONObject(it.toString())?.optString("color") }
            .mapNotNull { colorToGroupMap[it] }
            .groupingBy { it }
            .eachCount()

        val dominantGroup = groupCounts.maxByOrNull { it.value }?.key

        return if (dominantGroup != null && colorGroup.has(dominantGroup)) {
            val groupObject = colorGroup.getJSONObject(dominantGroup)
            val description = groupObject.getString("description")
            val details = groupObject.getString("details")
            Pair(description, details)
        } else {
            Pair("No dominant color group found.", "")
        }
    }

    fun nameToColorNumbers(name: String): List<Int> {
        return name.uppercase()
            .mapNotNull { LETTER_VALUES[it] } // skip characters not in map
            .toList()
    }

    private fun nameToIntArray(name: String): IntArray {
        return name.uppercase()
            .mapNotNull { LETTER_VALUES[it] } // skip characters not in map
            .toIntArray()
    }

    private fun sortedByCountFrequency(numbers: IntArray): List<Map.Entry<Int, Int>> {
        val sorted = numbers.toList().groupingBy { it }.eachCount()
            .entries
            .sortedWith(compareByDescending<Map.Entry<Int, Int>> { it.value }
                .thenBy { it.key })

        println("Sorted by frequency (desc) then by number (asc):")
        sorted.forEach { (number, count) ->
            println("$number -> $count times")
        }

        return sorted
    }

    private fun sortedByNumber(numbers: IntArray): List<Map.Entry<Int, Int>> {
        val sortedByFrequency = numbers.toList().groupingBy { it }.eachCount()
            .entries
            .sortedBy { it.key }

        println("Sorted by frequency (most repeated first):")
        sortedByFrequency.forEach { (number, count) ->
            println("$number -> $count times")
        }

        return sortedByFrequency
    }
}
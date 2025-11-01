package com.shambhu.myapplication.utils

import java.time.LocalDate
import java.util.Locale

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

    // Life Path Number Calculation
    fun calculateLifePath(date: LocalDate): Int {
        val day = date.dayOfMonth
        val month = date.monthValue
        val year = date.year

        // Reduce to single digit or master numbers
        var sum = day + month + year
        while (sum > 9) {
            sum = sum.toString().map { it.toString().toInt() }.sum()
        }
        return sum
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
        val letterValues = mapOf(
            'A' to 1, 'B' to 2, 'C' to 3, 'D' to 4, 'E' to 5, 'F' to 6, 'G' to 7, 'H' to 8, 'I' to 9,
            'J' to 1, 'K' to 2, 'L' to 3, 'M' to 4, 'N' to 5, 'O' to 6, 'P' to 7, 'Q' to 8, 'R' to 9,
            'S' to 1, 'T' to 2, 'U' to 3, 'V' to 4, 'W' to 5, 'X' to 6, 'Y' to 7, 'Z' to 8
        )

        val cleanedName = fullName.uppercase().filter { it in letterValues }
        val total = cleanedName.map { letterValues[it] ?: 0 }.sum()

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


}
package com.shambhu.myapplication.utils


import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class NumeroCalculator {

    fun calculateMulank(birthDate: String): Int {
        // Parse date string (assuming format: "dd/MM/yyyy")
        val dateParts = birthDate.split("/")
        val day = dateParts[0].toInt()

        return reduceToSingleDigit(day)
    }

    fun calculateBhagyank(birthDate: String): Int {
        // Parse date string and sum all digits
        val dateParts = birthDate.split("/")
        val day = dateParts[0].toInt()
        val month = dateParts[1].toInt()
        val year = dateParts[2].toInt()

        var sum = sumDigits(day) + sumDigits(month) + sumDigits(year)
        return reduceToSingleDigit(sum)
    }

    private fun sumDigits(number: Int): Int {
        var n = number
        var sum = 0
        while (n > 0) {
            sum += n % 10
            n /= 10
        }
        return sum
    }

    private fun reduceToSingleDigit(number: Int): Int {
        var n = number
        while (n > 9) {
            n = sumDigits(n)
        }
        return n
    }

    fun calculatePersonalYear(birthDate: String): Int {
        val dateParts = birthDate.split("/")
        val day = dateParts[0].toInt()
        val month = dateParts[1].toInt()
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)

        val sum = sumDigits(day) + sumDigits(month) + sumDigits(currentYear)
        return reduceToSingleDigit(sum)
    }

    fun calculatePersonalMonth(birthDate: String): Int {
        val personalYear = calculatePersonalYear(birthDate)
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1 // Calendar.MONTH is 0-based

        val sum = personalYear + currentMonth
        return reduceToSingleDigit(sum)
    }

    fun calculatePersonalDay(birthDate: String): Int {
        val personalMonth = calculatePersonalMonth(birthDate)
        val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        val sum = personalMonth + currentDay
        return reduceToSingleDigit(sum)
    }
}
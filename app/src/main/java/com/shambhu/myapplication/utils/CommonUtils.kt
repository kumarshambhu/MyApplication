package com.shambhu.myapplication.utils

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.Locale

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

    fun reduceNumber(n: Int): Int {
        /** Reduce number to single digit unless it's a master number. */
        if (n in listOf(11, 22, 33)) {
            return n
        }
        var number = n
        while (number > 9) {
            number = number.toString().map { it.toString().toInt() }.sum()
        }
        return number
    }
}
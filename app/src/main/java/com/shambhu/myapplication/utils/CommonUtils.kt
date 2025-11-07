package com.shambhu.myapplication.utils

import android.content.Context
import com.shambhu.myapplication.utils.Constants.Companion.LETTER_VALUES
import java.nio.charset.Charset
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

    fun readAssetFile(context: Context, filename: String): String {
        val inputStream =context.assets.open(filename)
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        return String(buffer, Charset.defaultCharset())
    }

    fun nameToIntArray(name: String): IntArray {
        return name.uppercase()
            .mapNotNull { LETTER_VALUES[it] } // skip characters not in map
            .toIntArray()
    }
}
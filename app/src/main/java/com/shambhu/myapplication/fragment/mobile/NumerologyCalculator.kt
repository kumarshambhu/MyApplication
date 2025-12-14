package com.shambhu.myapplication.fragment.mobile

import android.content.Context
import com.google.gson.Gson
import com.shambhu.myapplication.utils.CommonUtils
import com.shambhu.myapplication.utils.NumerologyCalculationUtils.convertToHtml
import java.text.SimpleDateFormat
import java.util.*

class NumerologyCalculator(private val context: Context) {

    data class MobileCombination(
        val combination: String,
        val state: String,
        val planets: List<String>,
        val traits: List<String>
    )

    data class SumGroup(
        val state: String,
        val group: List<Int>
    )

    data class MobileData(
        val combinations: List<MobileCombination>,
        val sum_of_mobile: List<SumGroup>
    )

    private lateinit var mobileData: MobileData

    init {
        loadMobileCombinations()
    }

    private fun loadMobileCombinations() {
        try {
            val jsonString = context.assets.open("mobile_combination.json")
                .bufferedReader()
                .use { it.readText() }

            val gson = Gson()
            mobileData = gson.fromJson(jsonString, MobileData::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // 1. Calculate Birthday Number
    fun calculateBirthdayNumber(dob: String): Int {
        return try {
            val date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(dob)
            val day = Calendar.getInstance().apply { time = date }.get(Calendar.DAY_OF_MONTH)
            reduceToSingleDigit(day)
        } catch (e: Exception) {
            // Try alternative format
            try {
                val parts = dob.split("/", "-", ".")
                if (parts.isNotEmpty()) {
                    reduceToSingleDigit(parts[0].toInt())
                } else {
                    0
                }
            } catch (ex: Exception) {
                0
            }
        }
    }

    // 2. Calculate Life Path Number
    fun calculateLifePathNumber(dob: String): Int {
        return try {
            val date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(dob)
            val calendar = Calendar.getInstance().apply { time = date }
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH) + 1
            val year = calendar.get(Calendar.YEAR)

            val daySum = reduceToSingleDigit(day)
            val monthSum = reduceToSingleDigit(month)
            val yearSum = reduceToSingleDigit(year)

            val total = daySum + monthSum + yearSum
            reduceToSingleDigit(total)
        } catch (e: Exception) {
            // Try alternative format
            try {
                val parts = dob.split("/", "-", ".")
                if (parts.size >= 3) {
                    val day = parts[0].toInt()
                    val month = parts[1].toInt()
                    val year = parts[2].toInt()

                    val daySum = reduceToSingleDigit(day)
                    val monthSum = reduceToSingleDigit(month)
                    val yearSum = reduceToSingleDigit(year)

                    val total = daySum + monthSum + yearSum
                    reduceToSingleDigit(total)
                } else {
                    0
                }
            } catch (ex: Exception) {
                0
            }
        }
    }

    // 3. Calculate Kua Number (Simplified - requires gender)
    fun calculateKuaNumber(dob: String, isMale: Boolean = true): Int {
        return try {
            val date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(dob)
            val calendar = Calendar.getInstance().apply { time = date }
            val year = calendar.get(Calendar.YEAR)

            val lastTwoDigits = year % 100

            if (isMale) {
                // For male: (100 - last two digits) reduced to single digit
                val kua = 100 - lastTwoDigits
                reduceToSingleDigit(kua)
            } else {
                // For female: (last two digits + 4) reduced to single digit
                val kua = lastTwoDigits + 4
                reduceToSingleDigit(kua)
            }
        } catch (e: Exception) {
            0
        }
    }

    // 4. Create Lo Shu Grid PROPERLY in 3x3 matrix
    fun createLoShuGrid(dob: String): Array<IntArray> {
        // Initialize 3x3 grid with zeros
        val grid = Array(3) { IntArray(3) { 0 } }

        // Extract all digits from DOB
        val digits = mutableListOf<Int>()
        dob.forEach { char ->
            if (char.isDigit()) {
                val digit = char.toString().toInt()
                if (digit in 1..9) {
                    digits.add(digit)
                }
            }
        }

        // Traditional Lo Shu Grid positions:
        // Standard magic square layout
        // 4 9 2
        // 3 5 7
        // 8 1 6

        // But in numerology, we fill based on number positions
        // Mapping: Number -> (row, col)
        val loShuPositions = mapOf(
            1 to Pair(2, 1), // Bottom center
            2 to Pair(0, 2), // Top right
            3 to Pair(1, 0), // Middle left
            4 to Pair(0, 0), // Top left
            5 to Pair(1, 1), // Center
            6 to Pair(2, 2), // Bottom right
            7 to Pair(1, 2), // Middle right
            8 to Pair(2, 0), // Bottom left
            9 to Pair(0, 1)  // Top center
        )

        // Count occurrences of each number (1-9)
        val numberCounts = IntArray(10) { 0 }
        digits.forEach { digit ->
            if (digit in 1..9) {
                numberCounts[digit]++
            }
        }

        // Fill the grid based on Lo Shu positions
        for (number in 1..9) {
            val position = loShuPositions[number]
            if (position != null) {
                val (row, col) = position
                grid[row][col] = numberCounts[number]
            }
        }

        return grid
    }

    // 5. Format Lo Shu Grid as String for display
    fun formatLoShuGrid(grid: Array<IntArray>): String {
        val builder = StringBuilder()

        builder.append("Lo Shu Grid (3x3):<br/>")
        builder.append("┌─────┬─────┬─────┐<br/>")

        for (i in 0 until 3) {
            builder.append("│")
            for (j in 0 until 3) {
                val count = grid[i][j]
                // Display number and count if count > 0
                val number = getNumberAtPosition(i, j)
                if (count > 0) {
                    builder.append("&nbsp;&nbsp;&nbsp;$number($count)&nbsp;&nbsp;&nbsp;")
                } else {
                    builder.append("&nbsp;&nbsp;&nbsp;$number(0)&nbsp;&nbsp;&nbsp;")
                }
                builder.append(" │")
            }
            if (i < 2) {
                builder.append("<br/>├─────┼─────┼─────┤<br/>")
            }
        }

        builder.append("<br/>└─────┴─────┴─────┘")

        // Add legend
        builder.append("<br/><br/>Legend: Number(Count in DOB)")
        builder.append("<br/>Traditional Lo Shu Layout:")
        builder.append("<br/>4 9 2")
        builder.append("<br/>3 5 7")
        builder.append("<br/>8 1 6")

        return convertToHtml(builder.toString())
    }

    // Helper to get number at grid position
    private fun getNumberAtPosition(row: Int, col: Int): Int {
        val positionMap = mapOf(
            Pair(0, 0) to 4,  // Top-left
            Pair(0, 1) to 9,  // Top-center
            Pair(0, 2) to 2,  // Top-right
            Pair(1, 0) to 3,  // Middle-left
            Pair(1, 1) to 5,  // Center
            Pair(1, 2) to 7,  // Middle-right
            Pair(2, 0) to 8,  // Bottom-left
            Pair(2, 1) to 1,  // Bottom-center
            Pair(2, 2) to 6   // Bottom-right
        )
        return positionMap[Pair(row, col)] ?: 0
    }

    // 6. Check Missing Numbers in Lo Shu Grid
    fun findMissingNumbers(grid: Array<IntArray>): List<Int> {
        val missingNumbers = mutableListOf<Int>()

        // Check each number position
        for (number in 1..9) {
            val position = when (number) {
                1 -> Pair(2, 1)  // Bottom center
                2 -> Pair(0, 2)  // Top right
                3 -> Pair(1, 0)  // Middle left
                4 -> Pair(0, 0)  // Top left
                5 -> Pair(1, 1)  // Center
                6 -> Pair(2, 2)  // Bottom right
                7 -> Pair(1, 2)  // Middle right
                8 -> Pair(2, 0)  // Bottom left
                9 -> Pair(0, 1)  // Top center
                else -> Pair(-1, -1)
            }

            val (row, col) = position
            if (row >= 0 && col >= 0 && grid[row][col] == 0) {
                missingNumbers.add(number)
            }
        }

        return missingNumbers
    }

    // 7. Get Analysis of Lo Shu Grid
    fun analyzeLoShuGrid(grid: Array<IntArray>): String {
        val builder = StringBuilder()

        // Check for strong numbers (appear 3 or more times)
        val strongNumbers = mutableListOf<Int>()
        val weakNumbers = mutableListOf<Int>()

        for (number in 1..9) {
            val count = when (number) {
                1 -> grid[2][1]
                2 -> grid[0][2]
                3 -> grid[1][0]
                4 -> grid[0][0]
                5 -> grid[1][1]
                6 -> grid[2][2]
                7 -> grid[1][2]
                8 -> grid[2][0]
                9 -> grid[0][1]
                else -> 0
            }

            when {
                count >= 3 -> strongNumbers.add(number)
                count == 0 -> weakNumbers.add(number)
            }
        }

        builder.append("Grid Analysis:\n")
        builder.append("Strong Numbers (≥3 times): ${strongNumbers.joinToString()}\n")
        builder.append("Weak/Missing Numbers: ${weakNumbers.joinToString()}")

        return builder.toString()
    }

    // 8. Sum of Mobile Number
    fun sumMobileNumber(mobile: String): Int {
        val digits = mobile.filter { it.isDigit() }
        var sum = 0

        digits.forEach { char ->
            sum += char.toString().toInt()
        }

        return reduceToSingleDigit(sum)
    }

    // 9. Get Mobile Sum State
    fun getMobileSumState(sum: Int): String {
        mobileData.sum_of_mobile.forEach { sumGroup ->
            if (sum in sumGroup.group) {
                return sumGroup.state
            }
        }
        return "Unknown"
    }

    // 10. Create Pairs from Mobile Number
    fun createMobilePairs(mobile: String): List<String> {
        // Remove all non-digits and zeros
        val cleanMobile = mobile.filter { it.isDigit() && it != '0' }
        val pairs = mutableListOf<String>()

        for (i in 0 until cleanMobile.length - 1) {
            val pair = cleanMobile.substring(i, i + 2)
            pairs.add(pair)
        }

        return pairs
    }

    // 11. Check Pair Combinations
    fun checkPairCombinations(pairs: List<String>): Map<String, String> {
        val results = mutableMapOf<String, String>()

        pairs.forEach { pair ->
            var found = false
            mobileData.combinations.forEach { combination ->
                val comboList = combination.combination.split(", ")
                if (pair in comboList) {
                    results[pair] = combination.state
                    found = true
                    return@forEach
                }
            }
            // If not found in combinations
            if (!found) {
                results[pair] = "Not Found"
            }
        }

        return results
    }

    // 12. Get Universal Benefic Pairs from mobile
    fun getUniversalBeneficPairs(pairs: List<String>): List<String> {
        val universalBeneficPairs = mutableListOf<String>()

        pairs.forEach { pair ->
            mobileData.combinations.forEach { combination ->
                if (combination.state == "universal benefic") {
                    val comboList = combination.combination.split(", ")
                    if (pair in comboList) {
                        universalBeneficPairs.add(pair)
                    }
                }
            }
        }

        return universalBeneficPairs
    }

    // Helper function to reduce to single digit
    private fun reduceToSingleDigit(number: Int): Int {
        var num = number
        while (num > 9 && num != 11 && num != 22 && num != 33) { // Keep master numbers
            var sum = 0
            var temp = num
            while (temp > 0) {
                sum += temp % 10
                temp /= 10
            }
            num = sum
        }
        return num
    }
}
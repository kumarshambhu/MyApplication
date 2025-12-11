package com.shambhu.myapplication.fragment.mobile

import android.content.Context
import com.google.gson.Gson
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
        val date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(dob)
        val day = Calendar.getInstance().apply { time = date }.get(Calendar.DAY_OF_MONTH)
        return reduceToSingleDigit(day)
    }

    // 2. Calculate Life Path Number
    fun calculateLifePathNumber(dob: String): Int {
        val date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(dob)
        val calendar = Calendar.getInstance().apply { time = date }
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)

        val daySum = reduceToSingleDigit(day)
        val monthSum = reduceToSingleDigit(month)
        val yearSum = reduceToSingleDigit(year)

        val total = daySum + monthSum + yearSum
        return reduceToSingleDigit(total)
    }

    // 3. Calculate Kua Number
    fun calculateKuaNumber(dob: String): Int {
        val date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(dob)
        val calendar = Calendar.getInstance().apply { time = date }
        val year = calendar.get(Calendar.YEAR)

        // For male: (100 - last two digits of year) reduced to single digit
        // For female: (last two digits of year + 4) reduced to single digit
        val lastTwoDigits = year % 100

        // Note: You'll need to add gender input to calculate properly
        // This is a simplified version
        val kua = (lastTwoDigits + 4) % 9
        return if (kua == 0) 9 else kua
    }

    // 4. Create Lo Shu Grid (3x3 magic square)
    fun createLoShuGrid(dob: String): Array<IntArray> {
        val grid = Array(3) { IntArray(3) }
        val numbers = mutableListOf<Int>()

        // Extract all digits from DOB
        dob.filter { it.isDigit() }.forEach { char ->
            numbers.add(char.toString().toInt())
        }

        // Count occurrences of each number 1-9
        val counts = IntArray(10)
        numbers.forEach { digit ->
            if (digit in 1..9) {
                counts[digit]++
            }
        }

        // Lo Shu grid positions:
        // 4 9 2
        // 3 5 7
        // 8 1 6

        // Fill grid with counts
        grid[0][0] = counts[4]
        grid[0][1] = counts[9]
        grid[0][2] = counts[2]
        grid[1][0] = counts[3]
        grid[1][1] = counts[5]
        grid[1][2] = counts[7]
        grid[2][0] = counts[8]
        grid[2][1] = counts[1]
        grid[2][2] = counts[6]

        return grid
    }

    // 5. Check Missing Numbers in Lo Shu Grid
    fun findMissingNumbers(grid: Array<IntArray>): List<Int> {
        val missingNumbers = mutableListOf<Int>()

        // Lo Shu grid positions mapping
        val positionMap = mapOf(
            Pair(0, 0) to 4,
            Pair(0, 1) to 9,
            Pair(0, 2) to 2,
            Pair(1, 0) to 3,
            Pair(1, 1) to 5,
            Pair(1, 2) to 7,
            Pair(2, 0) to 8,
            Pair(2, 1) to 1,
            Pair(2, 2) to 6
        )

        positionMap.forEach { (position, number) ->
            if (grid[position.first][position.second] == 0) {
                missingNumbers.add(number)
            }
        }

        return missingNumbers
    }

    // 6. Sum of Mobile Number
    fun sumMobileNumber(mobile: String): Int {
        // Remove all non-digits
        val digits = mobile.filter { it.isDigit() }
        var sum = 0

        digits.forEach { char ->
            sum += char.toString().toInt()
        }

        return reduceToSingleDigit(sum)
    }

    // 7. Get Mobile Sum State
    fun getMobileSumState(sum: Int): String {
        mobileData.sum_of_mobile.forEach { sumGroup ->
            if (sum in sumGroup.group) {
                return sumGroup.state
            }
        }
        return "Unknown"
    }

    // 8. Create Pairs from Mobile Number
    fun createMobilePairs(mobile: String): List<String> {
        val cleanMobile = mobile.filter { it.isDigit() && it != '0' }
        val pairs = mutableListOf<String>()

        for (i in 0 until cleanMobile.length - 1) {
            val pair = cleanMobile.substring(i, i + 2)
            pairs.add(pair)
        }

        return pairs
    }

    // 9. Check Pair Combinations
    fun checkPairCombinations(pairs: List<String>): Map<String, String> {
        val results = mutableMapOf<String, String>()

        pairs.forEach { pair ->
            mobileData.combinations.forEach { combination ->
                val comboList = combination.combination.split(", ")
                if (pair in comboList) {
                    results[pair] = combination.state
                    return@forEach
                }
            }
            // If not found in combinations, mark as "Not Found"
            if (!results.containsKey(pair)) {
                results[pair] = "Not Found"
            }
        }

        return results
    }

    // Helper function to reduce to single digit
    private fun reduceToSingleDigit(number: Int): Int {
        var num = number
        while (num > 9) {
            var sum = 0
            while (num > 0) {
                sum += num % 10
                num /= 10
            }
            num = sum
        }
        return num
    }
}
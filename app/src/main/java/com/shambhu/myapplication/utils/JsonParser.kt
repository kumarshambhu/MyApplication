// utils/JsonParser.kt
package com.shambhu.myapplication.utils

import android.content.Context
import com.shambhu.myapplication.model.NumerologyMobileCombination
import org.json.JSONObject

object JsonParser {
    fun parseCombinations(context: Context): List<NumerologyMobileCombination> {
        return try {
            val jsonString = context.assets.open("mobile_combination.json")
                .bufferedReader()
                .use { it.readText() }

            val jsonObject = JSONObject(jsonString)
            val combinationsArray = jsonObject.getJSONArray("combinations")

            val combinations = mutableListOf<NumerologyMobileCombination>()

            for (i in 0 until combinationsArray.length()) {
                val combinationObj = combinationsArray.getJSONObject(i)

                val planetsArray = combinationObj.getJSONArray("planets")
                val planetsList = mutableListOf<String>()
                for (j in 0 until planetsArray.length()) {
                    planetsList.add(planetsArray.getString(j))
                }

                val traitsArray = combinationObj.getJSONArray("traits")
                val traitsList = mutableListOf<String>()
                for (j in 0 until traitsArray.length()) {
                    traitsList.add(traitsArray.getString(j))
                }

                val combination = NumerologyMobileCombination(
                    combination = combinationObj.getString("combination"),
                    state = combinationObj.getString("state"),
                    planets = planetsList,
                    traits = traitsList
                )

                combinations.add(combination)
            }

            combinations
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    fun getCombinationByNumberPair(
        context: Context,
        numberPair: String
    ): NumerologyMobileCombination? {
        val combinations = parseCombinations(context)
        return combinations.find { combination ->
            combination.combination.split(", ").any { it == numberPair }
        }
    }

    fun getCombinationsForMultiplePairs(
        context: Context,
        pairs: List<String>
    ): List<Pair<String, NumerologyMobileCombination?>> {
        val combinations = parseCombinations(context)
        return pairs.map { pair ->
            val combination = combinations.find { combo ->
                combo.combination.split(", ").any { it == pair }
            }
            pair to combination
        }
    }

    fun getAllCombinationsForPairs(
        context: Context,
        pairs: List<String>
    ): List<NumerologyMobileCombination> {
        val combinations = parseCombinations(context)
        return pairs.mapNotNull { pair ->
            combinations.find { combo ->
                combo.combination.split(", ").any { it == pair }
            }
        }.distinctBy { it.combination }
    }

    /**
     * Creates all possible consecutive 2-digit pairs from a number string
     * Example: "9878" -> ["98", "87", "78"]
     */
    fun createPairsFromNumber(mobileNumber: String): List<String> {
        val pairs = mutableListOf<String>()
        val filteredNumber = mobileNumber.filter { it != '0' }

        if (filteredNumber.length < 2) {
            return pairs
        }


        for (i in 0..filteredNumber.length - 2) {
            val pair = filteredNumber.substring(i, i + 2)
            if (pair.length == 2 && pair.all { it.isDigit() }) {
                pairs.add(pair)
            }
        }

        return pairs
    }

    /**
     * Creates all possible 2-digit pairs from a number string (including non-consecutive)
     * Example: "9878" -> ["98", "97", "78", "87", "88", "77"]
     */
    fun createAllPairsFromNumber(number: String): List<String> {
        val pairs = mutableListOf<String>()
        val digits = number.toCharArray().distinct()

        for (i in digits.indices) {
            for (j in digits.indices) {
                val pair = "${digits[i]}${digits[j]}"
                pairs.add(pair)
            }
        }

        return pairs.distinct()
    }

    /**
     * Creates pairs with sliding window of given size
     */
    fun createSlidingPairs(number: String, windowSize: Int = 2): List<String> {
        return if (number.length < windowSize) {
            emptyList()
        } else {
            (0..number.length - windowSize).map { index ->
                number.substring(index, index + windowSize)
            }.filter { it.all { char -> char.isDigit() } }
        }
    }

    /**
     * Validates if input is a valid number for pair creation
     */
    fun isValidNumberForPairs(input: String): Boolean {
        return input.length >= 2 && input.all { it.isDigit() }
    }

    /**
     * Gets unique pairs from a number
     */
    fun getUniquePairs(number: String): List<String> {
        return createPairsFromNumber(number).distinct()
    }

    /**
     * Creates pairs and their reverse combinations
     * Example: "98" -> ["98", "89"]
     */
    fun createPairsWithReversals(number: String): List<String> {
        val pairs = createPairsFromNumber(number)
        val result = mutableListOf<String>()

        pairs.forEach { pair ->
            result.add(pair)
            if (pair[0] != pair[1]) { // Don't add reverse for same digits like "88"
                result.add(pair.reversed())
            }
        }

        return result.distinct()
    }
}
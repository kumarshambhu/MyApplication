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
}
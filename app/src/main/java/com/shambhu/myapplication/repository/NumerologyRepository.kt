package com.shambhu.myapplication.repository

import android.content.Context
import com.shambhu.myapplication.model.NumerologyMobileCombination
import org.json.JSONObject

class NumerologyRepository(private val context: Context) {

    fun parseCombinations(): List<NumerologyMobileCombination> {
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

    fun getCombinationByNumberPair(numberPair: String): NumerologyMobileCombination? {
        val combinations = parseCombinations()
        return combinations.find { combination ->
            combination.combination.split(", ").any { it == numberPair }
        }
    }

    fun getCombinationsForMultiplePairs(
        pairs: List<String>
    ): List<Pair<String, NumerologyMobileCombination?>> {
        val combinations = parseCombinations()
        return pairs.map { pair ->
            val combination = combinations.find { combo ->
                combo.combination.split(", ").any { it == pair }
            }
            pair to combination
        }
    }

    fun getAllCombinationsForPairs(
        pairs: List<String>
    ): List<NumerologyMobileCombination> {
        val combinations = parseCombinations()
        return pairs.mapNotNull { pair ->
            combinations.find { combo ->
                combo.combination.split(", ").any { it == pair }
            }
        }.distinctBy { it.combination }
    }

    fun getLifePathDescription(lifePath: Int): String {
        try {
            val inputStream = context.assets.open("life_path_meaning.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            val json = String(buffer, Charsets.UTF_8)
            val jsonObject = org.json.JSONObject(json)
            val lifePathObject = jsonObject.getJSONObject(lifePath.toString())
            var description = "<ul>"
            description += "<li>" + lifePathObject.getString("description") + "</li>"
            description += "<li>" + lifePathObject.getString("positive") + "</li>"
            description += "</ul>"
            return description
        } catch (ex: Exception) {
            ex.printStackTrace()
            return ""
        }
    }

    fun getElementsJson(): String {
        return context.assets.open("elements.json").bufferedReader().use { it.readText() }
    }

    fun getColorsJson(): String {
        return context.assets.open("colors.json").bufferedReader().use { it.readText() }
    }

    fun getNameCombinationJson(): String {
        return context.assets.open("name_combination.json").bufferedReader().use { it.readText() }
    }

    fun getDobCombinationJson(): String {
        return context.assets.open("dob_combination.json").bufferedReader().use { it.readText() }
    }

    fun getKarmicLessonDebtJson(): String {
        return context.assets.open("karmic_lesson_debt.json").bufferedReader().use { it.readText() }
    }
}

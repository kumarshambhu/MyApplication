package com.shambhu.myapplication.utils

import android.content.Context
import android.text.Html
import com.shambhu.myapplication.utils.Constants.Companion.LETTER_VALUES
import org.json.JSONArray

object NumerologyCalculationUtils {

    // Soul Urge (Heart's Desire) Number Calculation
    /* fun calculateSoulUrge(name: String): Int {
         val cleanedName = retainOnlyVowels(name).uppercase().filter { it in LETTER_VALUES }
         var total = cleanedName.map { LETTER_VALUES[it] ?: 0 }.sum()

         // Reduce to single digit or master numbers
         if (total == 11 || total == 22 || total == 33)
             return total
         while (total > 9) {
             total = total.toString().map { it.toString().toInt() }.sum()
         }
         return total
     }*/

    fun calculateSoulUrge(name: String, reduce: Boolean = true): Int {
        val cleanedName = retainOnlyVowels(name).uppercase().filter { it in LETTER_VALUES }
        val total = cleanedName.map { LETTER_VALUES[it] ?: 0 }.sum()
        if (total == 11 || total == 22 || total == 33)
            return total

        if (!reduce) return total

        // Reduce to single digit or master numbers
        return CommonUtils.reduceNumber(total)
    }

    // Personality Number Calculation
    fun calculatePersonality(name: String, reduce: Boolean = true): Int {
        val personalityCleanedName = removeVowels(name).uppercase().filter { it in LETTER_VALUES }
        val total = personalityCleanedName.map { LETTER_VALUES[it] ?: 0 }.sum()

        // Reduce to single digit or master numbers
        if (total == 11 || total == 22 || total == 33)
            return total
        if (!reduce) return total
        return CommonUtils.reduceNumber(total)
    }


    // Expression (Destiny) Number Calculation
    fun calculateExpression(name: String, reduce: Boolean = true): Int {
        // Convert name to all uppercase and remove spaces
        val cleanedName = name.uppercase().filter { it in LETTER_VALUES }

        var total = cleanedName.map { LETTER_VALUES[it] ?: 0 }.sum()
        // Reduce to single digit or master numbers
        if (total == 11 || total == 22 || total == 33)
            return total
        if (!reduce) return total
        return CommonUtils.reduceNumber(total)
    }

    fun calculateBirthdayNumber(day: Int, reduce: Boolean = true): Int {
        if (!reduce) return day
        return CommonUtils.reduceNumber(day)
    }

    // Life Path Number Calculation
    fun calculateLifePath(day: Int, month: Int, year: Int, reduce: Boolean = true): Int {
        val reducedDay = CommonUtils.reduceNumber(day)
        val reducedMonth = CommonUtils.reduceNumber(month)
        val reducedYear = CommonUtils.reduceNumber(year)
        val sum = reducedDay + reducedMonth + reducedYear
        if (!reduce) return sum
        // For challenge age calculation, we need a single digit Life Path number.
        return CommonUtils.reduceNumber(sum)
    }

    fun getLifePathDescription(context: Context, lifePath: Int): String {
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
            return NumerologyCalculationUtils.convertToHtml(description)
        } catch (ex: Exception) {
            ex.printStackTrace()
            return ""
        }
    }


    fun calculatePersonalYear(
        day: Int,
        month: Int,
        year: Int = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
    ): Int {
        val birthSum = CommonUtils.reduceNumber(day) + CommonUtils.reduceNumber(month)
        val yearSum = year.toString().map { it.toString().toInt() }.sum()
        return CommonUtils.reduceNumber(birthSum + yearSum)
    }

    fun calculatePersonalMonth(
        day: Int,
        month: Int,
        targetMonth: Int = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH) + 1
    ): Int {
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

    // Reduce to single digit unless it's a karmic debt number

    fun calculateKarmicDebtNumbers(
        day: Int,
        month: Int,
        year: Int,
        fullName: String
    ): List<Pair<String, Int>> {
        fun reduce(n: Int): Int {
            var num = n
            while (num > 9 && num != 11 && num != 22) {
                num = num.toString().map { it.toString().toInt() }.sum()
            }
            return num
        }

        val karmicDebtNumbers = listOf(13, 14, 16, 19)
        val results = mutableListOf<Pair<String, Int>>()

        val lifePathTotal = calculateLifePath(day, month, year, reduce = false)
        if (lifePathTotal in karmicDebtNumbers) {
            results.add("Life Path" to lifePathTotal)
        }

        val expressionTotal = calculateExpression(fullName, reduce = false)
        if (expressionTotal in karmicDebtNumbers) {
            results.add("Expression" to expressionTotal)
        }

        val soulUrgeTotal = calculateSoulUrge(fullName, reduce = false)
        if (soulUrgeTotal in karmicDebtNumbers) {
            results.add("Soul Urge" to soulUrgeTotal)
        }

        val personalityTotal = calculatePersonality(fullName, reduce = false)
        if (personalityTotal in karmicDebtNumbers) {
            results.add("Personality" to personalityTotal)
        }

        val birthdayTotal = calculateBirthdayNumber(day, reduce = false)
        if (birthdayTotal in karmicDebtNumbers) {
            results.add("Birthday" to birthdayTotal)
        }

        return results
    }


    fun calculateKarmicFromName(fullName: String): List<Int> {
        val nameNumbers = CommonUtils.nameToIntArray(fullName)
        val numList: MutableList<Int> = nameNumbers.toMutableList()
        val uniqueList = numList.distinct().toMutableList()

        return missingNumbers(uniqueList)
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

    fun getLuckyNumberDescription(context: Context, luckyNumber: Int): String {
        return try {
            val json = context.assets.open("lucky_number_meaning.json").bufferedReader().use { it.readText() }
            val jsonObject = org.json.JSONObject(json)
            val luckyNumberObject = jsonObject.getJSONObject(luckyNumber.toString())
            luckyNumberObject.getString("description")
        } catch (ex: Exception) {
            // In a real app, you'd want to log this error.
            // For now, we'll just return an empty string.
            ""
        }
    }

    fun calculateElements(fullName: String, jsonString: String): Pair<String, Map<String, Double>> {
        val nameNumbers = nameToIntArray(fullName)
        var dominantElement = ""
        val elementScores =
            mutableMapOf("AIR" to 0.0, "EARTH" to 0.0, "FIRE" to 0.0, "WATER" to 0.0)

        val jsonObject = org.json.JSONObject(jsonString)
        val elementMap = jsonObject.getJSONObject("element")
        val excessMap = jsonObject.getJSONObject("excess")

        for (number in nameNumbers) {
            if (elementMap.has(number.toString())) {
                val elements = elementMap.getJSONArray(number.toString())
                for (i in 0 until elements.length()) {
                    val elementObject = elements.getJSONObject(i)
                    val elementName = elementObject.getString("element")
                    val quantity = elementObject.getDouble("quantity")
                    elementScores[elementName] =
                        elementScores.getOrDefault(elementName, 0.0) + quantity

                    val highestElement = elementScores.maxByOrNull { it.value }?.key
                    if (highestElement != null && excessMap.has(highestElement)) {
                        dominantElement =
                            excessMap.getJSONObject(highestElement).getString("details")
                    } else {
                        dominantElement = "No dominant element found."
                    }
                }
            }
        }
        return Pair(dominantElement, elementScores)
    }

data class Quintuple<A, B, C, D, E>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D,
    val fifth: E
)
    fun calculateColorGroup(
        fullName: String,
        jsonString: String
    ): Quintuple<String, String, String, String, Int> {
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

        val userColors = nameNumbers.mapNotNull {
            colorByNumber.optJSONObject(it.toString())?.optString("color")
        }
        val groupCounts = userColors
            .mapNotNull { colorToGroupMap[it] }
            .groupingBy { it }
            .eachCount()

        val dominantGroup = groupCounts.maxByOrNull { it.value }?.key

        return if (dominantGroup != null && colorGroup.has(dominantGroup)) {
            val groupObject = colorGroup.getJSONObject(dominantGroup)
            val description = groupObject.getString("description")
            val details = groupObject.getString("details")

            val colorsInDominantGroup = mutableListOf<String>()
            val colorsArray = groupObject.getJSONArray("colors")
            for (i in 0 until colorsArray.length()) {
                colorsInDominantGroup.add(colorsArray.getString(i))
            }

            val matchedColors = userColors.filter { colorsInDominantGroup.contains(it) }.distinct()
            Quintuple(description, details, matchedColors.joinToString(", "), dominantGroup, matchedColors.size)

        } else {
            Quintuple("No dominant color group found.", "", "", "", 0)
        }
    }

    fun calculateColorCounts(fullName: String, jsonString: String): Map<String, Int> {
        val nameNumbers = nameToColorNumbers(fullName)
        val jsonObject = org.json.JSONObject(jsonString)
        val colorByNumber = jsonObject.getJSONObject("color_by_number")

        return nameNumbers
            .mapNotNull { number ->
                colorByNumber.optJSONObject(number.toString())?.optString("color")
            }
            .groupingBy { it }
            .eachCount()
    }

    fun findAllMatchedColorGroups(fullName: String, jsonString: String): Map<String, List<String>> {
        val nameNumbers = nameToColorNumbers(fullName)
        val jsonObject = org.json.JSONObject(jsonString)
        val colorByNumber = jsonObject.getJSONObject("color_by_number")
        val colorGroup = jsonObject.getJSONObject("color_group")

        val userColors = nameNumbers
            .mapNotNull { number ->
                colorByNumber.optJSONObject(number.toString())?.optString("color")
            }
            .distinct()

        val matchedGroups = mutableMapOf<String, List<String>>()
        val groupIterator = colorGroup.keys()

        while (groupIterator.hasNext()) {
            val groupName = groupIterator.next()
            val groupObject = colorGroup.getJSONObject(groupName)
            val colorsInGroupArray = groupObject.getJSONArray("colors")
            val colorsInGroup = List(colorsInGroupArray.length()) { i -> colorsInGroupArray.getString(i) }

            val matchedColors = userColors.filter { it in colorsInGroup }

            if (matchedColors.isNotEmpty()) {
                matchedGroups[groupName] = matchedColors
            }
        }
        return matchedGroups
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

    fun calculateCombinationNumber(
        jsonData: String,
        destiny: Int,
        soul: Int,
        personality: Int
    ): String? {
        val jsonArray = JSONArray(jsonData)
        for (i in 0 until jsonArray.length()) {
            val item = jsonArray.getJSONObject(i)
            if (item.getInt("destiny") == destiny &&
                item.getInt("soul") == soul &&
                item.getInt("personality") == personality
            ) {
                return convertToHtml(item.getString("summary"))
            }
        }
        return null
    }

    fun retainOnlyVowels(input: String): String {
        return input.replace(Regex("[^aeiouAEIOU]"), "")
    }

    fun removeVowels(input: String): String {
        return input.replace(Regex("[aeiouAEIOU]"), "")
    }

    fun convertToHtml(input: String): String {
        return Html.fromHtml(input, Html.FROM_HTML_MODE_LEGACY).toString()
    }

    fun missingNumbers(numList: MutableList<Int>): List<Int> {
        val fullRange = (1..9).toSet()
        val present = numList.toSet()
        return (fullRange - present).toList().sorted()
    }
}
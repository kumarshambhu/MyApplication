package com.shambhu.myapplication.utils

import android.content.Context
import android.text.Html
import com.example.myapplication.NameAnalyzer
import com.shambhu.myapplication.model.ColorAnalysisResult
import com.shambhu.myapplication.model.ColorsData
import com.shambhu.myapplication.model.ElementAnalysisResult
import com.shambhu.myapplication.model.ElementData
import com.shambhu.myapplication.model.KarmicDebtItem
import com.shambhu.myapplication.model.KarmicLessonItem
import com.shambhu.myapplication.model.LifePathCycle
import com.shambhu.myapplication.model.LoshuGridPlanes
import com.shambhu.myapplication.utils.Constants.Companion.LETTER_VALUES
import org.json.JSONObject

object NumerologyCalculationUtils {

    fun calculateNameAnalysisGrid(name: String): NameAnalyzer.NameAnalysisResult {
        val analyzer = NameAnalyzer()
        val result = analyzer.analyzeName(name)
        return result
    }

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

    fun calculateSuccessNumber(day: Int, month: Int): Int {
        return CommonUtils.reduceNumber(day + month)
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
    ): List<KarmicDebtItem> {
        val karmicDebtNumbers = listOf(13, 14, 16, 19)
        val results = mutableListOf<KarmicDebtItem>()

        val lifePathTotal = calculateLifePath(day, month, year, reduce = false)
        if (lifePathTotal in karmicDebtNumbers) {
            results.add(KarmicDebtItem("Life Path", lifePathTotal))
        }

        val expressionTotal = calculateExpression(fullName, reduce = false)
        if (expressionTotal in karmicDebtNumbers) {
            results.add(KarmicDebtItem("Expression", expressionTotal))
        }

        val soulUrgeTotal = calculateSoulUrge(fullName, reduce = false)
        if (soulUrgeTotal in karmicDebtNumbers) {
            results.add(KarmicDebtItem("Soul Urge", soulUrgeTotal))
        }

        val personalityTotal = calculatePersonality(fullName, reduce = false)
        if (personalityTotal in karmicDebtNumbers) {
            results.add(KarmicDebtItem("Personality", personalityTotal))
        }

        val birthdayTotal = calculateBirthdayNumber(day, reduce = false)
        if (birthdayTotal in karmicDebtNumbers) {
            results.add(KarmicDebtItem("Birthday", birthdayTotal))
        }

        return results
    }


    fun calculateKarmicFromName(context: Context, fullName: String): List<KarmicLessonItem> {
        val nameNumbers = CommonUtils.nameToIntArray(fullName)
        val numList: MutableList<Int> = nameNumbers.toMutableList()
        val uniqueList = numList.distinct().toMutableList()
        val missing = missingNumbers(uniqueList)

        val karmicLessonsJson = CommonUtils.readAssetFile(context, "karmic_lesson_debt.json")
        val karmicLessonsObject = JSONObject(karmicLessonsJson).getJSONObject("karmic_lesson")

        return missing.map { number ->
            val detail =
                karmicLessonsObject.optString(number.toString(), "No description available.")
            KarmicLessonItem(number, detail)
        }
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

    fun calculatePrimaryLuckyNumbers(
        day: Int,
        month: Int,
        year: Int,
        fullName: String
    ): List<Pair<String, Int>> {
        return listOf(
            "Life Path" to calculateLifePath(day, month, year),
            "Expression" to calculateExpression(fullName),
            "Soul Urge" to calculateSoulUrge(fullName),
            "Personality" to calculatePersonality(fullName),
            "Birthday" to calculateBirthdayNumber(day)
        )
    }

    fun calculateElements(fullName: String, elementData: ElementData): ElementAnalysisResult {
        val nameNumbers = nameToIntArray(fullName)
        var dominantElementDescription = ""
        var dominantElementKey = ""
        var dominantDefinitionDescription = ""
        var dominantDefinitionDetail = ""
        val elementScores =
            mutableMapOf("AIR" to 0.0, "EARTH" to 0.0, "FIRE" to 0.0, "WATER" to 0.0)

        val elementMap = elementData.element
        val excessMap = elementData.excess
        val definitionMap = elementData.definition


        for (number in nameNumbers) {
            elementMap[number.toString()]?.forEach { elementInfo ->
                val elementName = elementInfo.element
                val quantity = elementInfo.quantity
                elementScores[elementName] =
                    elementScores.getOrDefault(elementName, 0.0) + quantity

                val highestElement = elementScores.maxByOrNull { it.value }?.key
                if (highestElement != null && excessMap.containsKey(highestElement)) {
                    dominantElementKey = highestElement
                    dominantElementDescription =
                        excessMap[highestElement]?.details ?: "No dominant element found."
                    dominantDefinitionDescription =
                        definitionMap[highestElement]?.description ?: "No dominant element found."
                    dominantDefinitionDetail =
                        definitionMap[highestElement]?.details ?: "No dominant element found."
                } else {
                    dominantElementDescription = "No dominant element found."
                }
            }
        }
        return ElementAnalysisResult(
            dominantElementKey,
            dominantElementDescription,
            dominantDefinitionDescription,
            dominantDefinitionDetail,
            elementScores
        )
    }

    fun calculateColorGroup(
        fullName: String,
        colorsData: ColorsData
    ): ColorAnalysisResult {
        val nameNumbers = nameToColorNumbers(fullName)
        val colorByNumber = colorsData.colorByNumber
        val colorGroup = colorsData.colorGroup

        val colorToGroupMap = mutableMapOf<String, String>()
        for ((groupName, groupDetails) in colorGroup) {
            for (colorName in groupDetails.colors) {
                colorToGroupMap[colorName] = groupName
            }
        }

        val userColors = nameNumbers.mapNotNull {
            colorByNumber[it.toString()]?.color
        }
        val groupCounts = userColors
            .mapNotNull { colorToGroupMap[it] }
            .groupingBy { it }
            .eachCount()

        val dominantGroup = groupCounts.maxByOrNull { it.value }?.key

        return if (dominantGroup != null && colorGroup.containsKey(dominantGroup)) {
            val groupObject = colorGroup[dominantGroup]!!
            val description = groupObject.description
            val details = groupObject.details

            val colorsInDominantGroup = groupObject.colors

            val matchedColors = userColors.filter { colorsInDominantGroup.contains(it) }.distinct()
            ColorAnalysisResult(
                description = description,
                details = details,
                matchedColors = matchedColors.joinToString(", "),
                group = dominantGroup,
                matchedColorsCount = matchedColors.size
            )

        } else {
            ColorAnalysisResult(
                description = "No dominant color group found.",
                details = "",
                matchedColors = "",
                group = "",
                matchedColorsCount = 0
            )
        }
    }

    fun calculateColorCounts(fullName: String, colorsData: ColorsData): Map<String, Int> {
        val nameNumbers = nameToColorNumbers(fullName)
        val colorByNumber = colorsData.colorByNumber

        return nameNumbers
            .mapNotNull { number ->
                colorByNumber[number.toString()]?.color
            }
            .groupingBy { it }
            .eachCount()
    }

    fun findAllMatchedColorGroups(
        fullName: String,
        colorsData: ColorsData
    ): Map<String, List<String>> {
        val nameNumbers = nameToColorNumbers(fullName)
        val colorByNumber = colorsData.colorByNumber
        val colorGroup = colorsData.colorGroup

        val userColors = nameNumbers
            .mapNotNull { number ->
                colorByNumber[number.toString()]?.color
            }
            .distinct()

        val matchedGroups = mutableMapOf<String, List<String>>()

        for ((groupName, groupDetails) in colorGroup) {
            val colorsInGroup = groupDetails.colors
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

    fun calculateCombinationNameNumber(
        jsonData: String,
        destiny: Int,
        soul: Int,
        personality: Int
    ): String? {
        val jsonObject = JSONObject(jsonData)
        val jsonArray = jsonObject.getJSONArray("name_combination")
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


    fun calculateCombinationDobNumber(
        jsonData: String,
        mulank: Int,
        bhagyank: Int
    ): Pair<String, String> {
        val jsonObject = JSONObject(jsonData)
        val jsonArray = jsonObject.getJSONArray("dob_combination")
        for (i in 0 until jsonArray.length()) {
            val item = jsonArray.getJSONObject(i)
            if (item.getInt("mulank") == mulank) {
                val jsonObject1 = JSONObject(item.toString())
                val jsonArray1 = jsonObject1.getJSONArray("combinations")
                for (j in 0 until jsonArray1.length()) {
                    val item1 = jsonArray1.getJSONObject(j)
                    if (item1.getInt("bhagyank") == bhagyank)
                        return Pair(item1.getString("remark"), item1.getString("luck"))
                }
            }
        }
        return Pair("", "")
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

    fun calculateLoshuGridPlanes(numberCounts: IntArray): LoshuGridPlanes {
        fun getAvailableNumbersInPlane(planeNumbers: List<Int>): List<Int> {
            return planeNumbers.filter {
                println(it)
                numberCounts[it] != 0
            }
        }

        return LoshuGridPlanes(
            mentalPlane = getAvailableNumbersInPlane(listOf(4, 9, 2)),
            emotionalPlane = getAvailableNumbersInPlane(listOf(3, 5, 7)),
            practicalPlane = getAvailableNumbersInPlane(listOf(8, 1, 6)),
            thoughtPlane = getAvailableNumbersInPlane(listOf(4, 3, 8)),
            willPlane = getAvailableNumbersInPlane(listOf(9, 5, 1)),
            actionPlane = getAvailableNumbersInPlane(listOf(2, 7, 6)),
            silverSuccessPlane = getAvailableNumbersInPlane(listOf(4, 5, 6)),
            goldenSuccessPlane = getAvailableNumbersInPlane(listOf(2, 5, 8))
        )
    }

    fun calculateKuaNumber(birthYear: Int, isMale: Boolean): Int {
        if (birthYear < 1900 || birthYear > 2100) return -1
        val lastTwoDigits = birthYear % 100
        val sumOfYear = CommonUtils.reduceNumberIgnoreMasterNumber(lastTwoDigits)

        val baseNumber: Int = if (birthYear < 2000) {
            10 // Base for years 1900-1999
        } else {
            9  // Base for years 2000+
        }

        val kuaNumber: Int = if (isMale) {
            baseNumber - sumOfYear
        } else {
            if (birthYear < 2000) {
                5 + sumOfYear
            } else {
                6 + sumOfYear
            }
        }

        return CommonUtils.reduceNumberIgnoreMasterNumber(kuaNumber)
    }

    fun calculateMaturityNumber(lifePath: Int, destiny: Int): Int {
        return CommonUtils.reduceNumber(lifePath + destiny)
    }

    fun calculateLifePathCycles(
        context: Context,
        day: Int,
        month: Int,
        year: Int
    ): List<LifePathCycle> {
        val lifePathNumber = calculateLifePath(day, month, year)

        val firstCycleNumber = CommonUtils.reduceNumber(month)
        val secondCycleNumber = CommonUtils.reduceNumber(day)
        val thirdCycleNumber = CommonUtils.reduceNumber(year)

        val endOfFirstCycle = 36 - lifePathNumber
        val startOfSecondCycle = endOfFirstCycle + 1
        val endOfSecondCycle = endOfFirstCycle + 27
        val startOfThirdCycle = endOfSecondCycle + 1

        val cyclesJson = CommonUtils.readAssetFile(context, "life_path_cycle.json")
        val cyclesObject = org.json.JSONObject(cyclesJson)

        val cycles = mutableListOf<LifePathCycle>()

        cycles.add(
            LifePathCycle(
                title = "First Life Cycle",
                ageRange = "Ages 0 - $endOfFirstCycle",
                cycleNumber = firstCycleNumber,
                description = cyclesObject.optString(firstCycleNumber.toString())
            )
        )
        cycles.add(
            LifePathCycle(
                title = "Second Life Cycle",
                ageRange = "Ages $startOfSecondCycle - $endOfSecondCycle",
                cycleNumber = secondCycleNumber,
                description = cyclesObject.optString(secondCycleNumber.toString())
            )
        )
        cycles.add(
            LifePathCycle(
                title = "Third Life Cycle",
                ageRange = "Ages $startOfThirdCycle onwards",
                cycleNumber = thirdCycleNumber,
                description = cyclesObject.optString(thirdCycleNumber.toString())
            )
        )

        return cycles
    }
}

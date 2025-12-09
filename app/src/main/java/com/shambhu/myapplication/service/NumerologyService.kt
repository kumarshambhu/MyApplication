package com.shambhu.myapplication.service

import com.shambhu.myapplication.repository.NumerologyRepository
import com.shambhu.myapplication.utils.CommonUtils
import com.shambhu.myapplication.utils.Constants

class NumerologyService(private val repository: NumerologyRepository) {
    fun calculateNameAnalysisGrid(name: String): com.example.myapplication.NameAnalyzer.NameAnalysisResult {
        val analyzer = com.example.myapplication.NameAnalyzer()
        val result = analyzer.analyzeName(name)
        return result
    }

    fun calculateSoulUrge(name: String, reduce: Boolean = true): Int {
        val cleanedName = retainOnlyVowels(name).uppercase().filter { it in Constants.LETTER_VALUES }
        val total = cleanedName.map { Constants.LETTER_VALUES[it] ?: 0 }.sum()
        if (total == 11 || total == 22 || total == 33)
            return total

        if (!reduce) return total

        return CommonUtils.reduceNumber(total)
    }

    fun calculatePersonality(name: String, reduce: Boolean = true): Int {
        val personalityCleanedName = removeVowels(name).uppercase().filter { it in Constants.LETTER_VALUES }
        val total = personalityCleanedName.map { Constants.LETTER_VALUES[it] ?: 0 }.sum()

        if (total == 11 || total == 22 || total == 33)
            return total
        if (!reduce) return total
        return CommonUtils.reduceNumber(total)
    }

    fun calculateExpression(name: String, reduce: Boolean = true): Int {
        val cleanedName = name.uppercase().filter { it in Constants.LETTER_VALUES }

        var total = cleanedName.map { Constants.LETTER_VALUES[it] ?: 0 }.sum()
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

    fun calculateLifePath(day: Int, month: Int, year: Int, reduce: Boolean = true): Int {
        val reducedDay = CommonUtils.reduceNumber(day)
        val reducedMonth = CommonUtils.reduceNumber(month)
        val reducedYear = CommonUtils.reduceNumber(year)
        val sum = reducedDay + reducedMonth + reducedYear
        if (!reduce) return sum
        return CommonUtils.reduceNumber(sum)
    }

    fun getLifePathDescription(lifePath: Int): String {
        val description = repository.getLifePathDescription(lifePath)
        return convertToHtml(description)
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

    fun calculateKarmicDebtNumbers(
        day: Int,
        month: Int,
        year: Int,
        fullName: String
    ): List<Pair<String, Int>> {
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

    fun calculatePrimaryLuckyNumbers(day: Int, month: Int, year: Int, fullName: String): List<Pair<String, Int>> {
        return listOf(
            "Life Path" to calculateLifePath(day, month, year),
            "Expression" to calculateExpression(fullName),
            "Soul Urge" to calculateSoulUrge(fullName),
            "Personality" to calculatePersonality(fullName),
            "Birthday" to calculateBirthdayNumber(day)
        )
    }

    fun calculateElements(fullName: String): Pair<String, Map<String, Double>> {
        val nameNumbers = nameToIntArray(fullName)
        var dominantElement = ""
        val elementScores =
            mutableMapOf("AIR" to 0.0, "EARTH" to 0.0, "FIRE" to 0.0, "WATER" to 0.0)

        val jsonString = repository.getElementsJson()
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

    fun calculateColorGroup(
        fullName: String
    ): Quintuple<String, String, String, String, Int> {
        val nameNumbers = nameToColorNumbers(fullName)
        val jsonString = repository.getColorsJson()
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

    fun calculateColorCounts(fullName: String): Map<String, Int> {
        val nameNumbers = nameToColorNumbers(fullName)
        val jsonString = repository.getColorsJson()
        val jsonObject = org.json.JSONObject(jsonString)
        val colorByNumber = jsonObject.getJSONObject("color_by_number")

        return nameNumbers
            .mapNotNull { number ->
                colorByNumber.optJSONObject(number.toString())?.optString("color")
            }
            .groupingBy { it }
            .eachCount()
    }

    fun findAllMatchedColorGroups(fullName: String): Map<String, List<String>> {
        val nameNumbers = nameToColorNumbers(fullName)
        val jsonString = repository.getColorsJson()
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
            .mapNotNull { Constants.LETTER_VALUES[it] }
            .toList()
    }

    private fun nameToIntArray(name: String): IntArray {
        return name.uppercase()
            .mapNotNull { Constants.LETTER_VALUES[it] }
            .toIntArray()
    }

    fun calculateCombinationNameNumber(
        destiny: Int,
        soul: Int,
        personality: Int
    ): String? {
        val jsonData = repository.getNameCombinationJson()
        val jsonObject = org.json.JSONObject(jsonData)
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
        mulank: Int,
        bhagyank: Int
    ): Pair<String, String> {
        val jsonData = repository.getDobCombinationJson()
        val jsonObject = org.json.JSONObject(jsonData)
        val jsonArray = jsonObject.getJSONArray("dob_combination")
        for (i in 0 until jsonArray.length()) {
            val item = jsonArray.getJSONObject(i)
            if (item.getInt("mulank") == mulank) {
                val jsonObject1 = org.json.JSONObject(item.toString())
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
        return android.text.Html.fromHtml(input, android.text.Html.FROM_HTML_MODE_LEGACY).toString()
    }

    fun missingNumbers(numList: MutableList<Int>): List<Int> {
        val fullRange = (1..9).toSet()
        val present = numList.toSet()
        return (fullRange - present).toList().sorted()
    }

    fun calculateLoshuGridPlanes(numberCounts: IntArray): com.shambhu.myapplication.model.LoshuGridPlanes {
        fun getAvailableNumbersInPlane(planeNumbers: List<Int>): List<Int> {
            return planeNumbers.filter {
                println(it)
                numberCounts[it] != 0
            }
        }

        return com.shambhu.myapplication.model.LoshuGridPlanes(
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
            10
        } else {
            9
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

    fun calculateMulank(birthDate: String): Int {
        val dateParts = birthDate.split("/")
        val day = dateParts[0].toInt()

        return reduceToSingleDigit(day)
    }

    fun calculateBhagyank(birthDate: String): Int {
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

    fun getKarmicLessonDebtJson(): String {
        return repository.getKarmicLessonDebtJson()
    }
}

data class Quintuple<A, B, C, D, E>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D,
    val fifth: E
)

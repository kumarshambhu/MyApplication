package com.shambhu.myapplication.repository

import android.content.Context

class NumerologyRepository(private val context: Context) {

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

    fun getBirthdayJson(): String {
        return context.assets.open("birthday.json").bufferedReader().use { it.readText() }
    }

    fun getSoulUrgeJson(): String {
        return context.assets.open("soul_urge.json").bufferedReader().use { it.readText() }
    }

    fun getPersonalityJson(): String {
        return context.assets.open("personality.json").bufferedReader().use { it.readText() }
    }

    fun getDestinyJson(): String {
        return context.assets.open("destiny.json").bufferedReader().use { it.readText() }
    }

    fun getMissingNumberJson(): String {
        return context.assets.open("missing_number.json").bufferedReader().use { it.readText() }
    }

    fun getRepeatingNumberJson(): String {
        return context.assets.open("repeate_number.json").bufferedReader().use { it.readText() }
    }

    fun getPlaneJson(): String {
        return context.assets.open("plane.json").bufferedReader().use { it.readText() }
    }
}

package com.shambhu.myapplication.model

import com.google.gson.annotations.SerializedName

// FavorablePeriod.kt

// MulankData.kt
data class MulankData(
    val id: Int,
    val name: String,
    val characteristics: List<String>,
    val strengths: List<String>,
    val weaknesses: List<String>,
    val advice: List<String>,


    @SerializedName("birth_dates")
    val birthDates: List<Int>,

    @SerializedName("ruling_planet")
    val rulingPlanet: String,

    @SerializedName("favorable_periods")
    val favorablePeriods: List<NumeroData.Period>,

    @SerializedName("unfavorable_periods")
    val unfavorablePeriods: List<NumeroData.Period>,

    @SerializedName("lucky_colors")
    val luckyColors: List<String>,

    @SerializedName("color_usage_tips")
    val colorUsageTips: List<String>
)


data class BhagyankData(
    val id: Int,
    val name: String,

    @SerializedName("ruling_planet")
    val rulingPlanet: String,

    val traits: List<String>,
    val advice: List<String>,

    @SerializedName("career_suggestions")
    val careerSuggestions: List<String>,

    @SerializedName("gender_specific")
    val genderSpecific: Map<String, List<String>>? = null
)


data class Combination(
    val combination: String,
    val planets: String,
    val character: String,
    val career: String,
    val lucky: String,
    val health: String,
    val warning: String,
    val solution: String,
    val rating: String,
    val traits: String,
    val luck: String,
    val remark: String

)

// MulankBhagyankCombination.kt
data class MulankBhagyankCombination(
    val day_number: Int,
    val ruler: String,
    val combinations: List<Combination>
)

// MulankBhagyankResponse.kt
data class MulankBhagyankResponse(
    val mulank_bhagyank_combinations: List<MulankBhagyankCombination>
)

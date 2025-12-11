package com.shambhu.myapplication.model

import com.google.gson.annotations.SerializedName

data class PersonalDay(
    @SerializedName("day_number") val dayNumber: Int,
    val description: String,
    @SerializedName("social_hints") val socialHints: List<String>,
    @SerializedName("lucky_colors") val luckyColors: List<String>
)

data class PersonalMonth(
    @SerializedName("month_number") val monthNumber: Int,
    val positive: List<String>,
    val negative: List<String>,
    @SerializedName("enhancement_tips") val enhancementTips: List<String>
)

data class PersonalYear(
    @SerializedName("year_number") val yearNumber: Int,
    val title: String,
    @SerializedName("positive_outcomes") val positiveOutcomes: List<String>,
    @SerializedName("negative_impacts") val negativeImpacts: List<String>
)

data class PersonalDayResponse(
    @SerializedName("personal_days") val personalDays: List<PersonalDay>
)

data class PersonalMonthResponse(
    @SerializedName("personal_months") val personalMonths: List<PersonalMonth>
)

data class PersonalYearResponse(
    @SerializedName("personal_years") val personalYears: List<PersonalYear>
)

data class PersonalFortuneData(
    val personalDay: PersonalDay?,
    val personalMonth: PersonalMonth?,
    val personalYear: PersonalYear?
)

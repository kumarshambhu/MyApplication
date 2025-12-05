package com.shambhu.myapplication.model



data class NumeroData(
    val name: String = "",
    val rulingPlanet: String = "",
    val birthDates: List<Int> = emptyList(),
    val characteristics: List<String> = emptyList(),
    val strengths: List<String> = emptyList(),
    val weaknesses: List<String> = emptyList(),
    val advice: List<String> = emptyList(),
    val favorablePeriods: List<Period>? = null,
    val unfavorablePeriods: List<Period>? = null,
    val luckyColors: List<String> = emptyList(),
    val colorUsageTips: List<String> = emptyList(),
    val traits: List<String> = emptyList(),
    val careerSuggestions: List<String> = emptyList(),
    val genderSpecific: Map<String, List<String>>? = null
) {
    data class Period(
        val time: String,
        val description: String
    )
}
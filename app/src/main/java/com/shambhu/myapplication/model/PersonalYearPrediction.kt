package com.shambhu.myapplication.model

data class PersonalYearPredictionResponse(
    val personal_years: List<PersonalYear>
)

data class PersonalYear(
    val year_number: Int,
    val title: String,
    val positive_outcomes: List<String>,
    val negative_impacts: List<String>
)

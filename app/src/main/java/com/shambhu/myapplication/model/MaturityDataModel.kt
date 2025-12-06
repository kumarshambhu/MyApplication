package com.shambhu.myapplication.model

data class MaturityDataModel (
    val overview: String,
    val positive_traits: List<String>,
    val challenges: List<String>,
    val karmic_notes: String? = null,
    val life_purpose: String? = null,
    val life_outcome: String? = null
)

data class MaturityNumbersResponse(
    val maturity_numbers: Map<String, MaturityDataModel>
)
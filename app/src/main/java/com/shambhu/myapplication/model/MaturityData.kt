package com.shambhu.myapplication.model

import com.google.gson.annotations.SerializedName

data class MaturityDataResponse(
    @SerializedName("maturity_numbers")
    val maturityNumbers: Map<String, MaturityData>
)

data class MaturityData(
    val overview: String,
    @SerializedName("positive_traits")
    val positiveTraits: List<String>,
    val challenges: List<String>,
    @SerializedName("karmic_notes")
    val karmicNotes: String?,
    @SerializedName("life_purpose")
    val lifePurpose: String?,
    @SerializedName("life_outcome")
    val lifeOutcome: String?
)

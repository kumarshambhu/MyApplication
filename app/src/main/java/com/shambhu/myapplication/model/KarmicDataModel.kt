package com.shambhu.myapplication.model

// Data class for a single key to overcome
data class KarmicKey(
    val key: String,
    val description: String
)

// Data class for each karmic debt number
data class KarmicDebt(
    val number: String,
    val source: String,
    val challengesAndProblems: List<String>?,
    val qualities: List<String>?,
    val keysToOvercome: List<KarmicKey>?,
    val summary: String?,
    val potentialOutcome: String? = null,
    var isExpanded: Boolean = false
)


data class KarmicDebtResponse(
    val karmic_debt: Map<String, KarmicDebt>
)
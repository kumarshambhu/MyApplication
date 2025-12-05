package com.shambhu.myapplication.fragment.others

data class Combination(
    val combination: String,
    val planets: String,
    val character: String,
    val career: String,
    val lucky: String,
    val health: String,
    val warning: String,
    val solution: String,
    val rating: String
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

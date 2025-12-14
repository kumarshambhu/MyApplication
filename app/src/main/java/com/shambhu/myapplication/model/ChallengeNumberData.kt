package com.shambhu.myapplication.model

data class ChallengeNumber(
    val number: Int,
    val interpretation: String
)

data class ChallengeNumberData(
    val challenge_numbers: List<ChallengeNumber>
)

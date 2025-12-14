package com.shambhu.myapplication.model

data class PinnacleNumber(
    val number: Int,
    val interpretation: String
)

data class PinnacleNumberData(
    val pinnacle_numbers: List<PinnacleNumber>
)

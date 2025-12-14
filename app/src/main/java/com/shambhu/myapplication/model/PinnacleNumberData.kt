package com.shambhu.myapplication.model

import com.google.gson.annotations.SerializedName

data class PinnacleNumber(
    val number: Int,
    val details: String
)

data class PinnacleNumberData(
    @SerializedName("pinnacle_numbers")
    val pinnacleNumbers: List<PinnacleNumber>
)

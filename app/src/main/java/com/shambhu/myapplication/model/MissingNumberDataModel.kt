package com.shambhu.myapplication.model

import com.google.gson.annotations.SerializedName

data class MissingNumberData(
    @SerializedName("missing_numbers")
    val missingNumbers: List<MissingNumber>
)

data class MissingNumber(
    val number: Int,
    val impacts: List<String>,
    val remedies: List<String>
)

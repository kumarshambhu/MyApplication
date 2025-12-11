package com.shambhu.myapplication.model

import com.google.gson.annotations.SerializedName


data class RepetitiveNumberData(
    @SerializedName("program_title")
    val programTitle: String,
    val author: String,
    @SerializedName("repetitive_numbers")
    val repetitiveNumbers: List<RepetitiveNumber>
)

data class RepetitiveNumber(
    val number: Int,
    val symbolism: String? = null,
    val occurrences: List<Occurrence>
)

data class Occurrence(
    val count: String, // Can be "1", "2", "3", "4 or 5", etc.
    val effects: List<String>
)

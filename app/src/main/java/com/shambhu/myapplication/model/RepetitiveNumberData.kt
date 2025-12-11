package com.shambhu.myapplication.model

import com.google.gson.annotations.SerializedName

data class RepetitiveNumberData(
    @SerializedName("repetitive_numbers")
    val repetitiveNumbers: List<RepetitiveNumber>
)

data class RepetitiveNumber(
    val number: Int,
    val occurrences: List<Occurrence>
)

data class Occurrence(
    val count: OccurrenceCount,
    val effects: List<String>
)

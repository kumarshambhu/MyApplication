package com.shambhu.myapplication.model

data class SuccessNumberResponse(
    val success_numbers: Map<String, SuccessNumberData>
)

data class SuccessNumberData(
    val qualities: List<String>,
    val challenges: List<String>,
    val notes: String? = null
)

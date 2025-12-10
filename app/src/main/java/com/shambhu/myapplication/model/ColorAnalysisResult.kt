package com.shambhu.myapplication.model

data class ColorAnalysisResult(
    val description: String,
    val details: String,
    val matchedColors: String,
    val group: String,
    val matchedColorsCount: Int
)

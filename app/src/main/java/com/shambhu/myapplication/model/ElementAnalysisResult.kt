package com.shambhu.myapplication.model

data class ElementAnalysisResult(
    val dominantElementKey: String,
    val dominantElementDescription: String,
    val elementScores: Map<String, Double>
)

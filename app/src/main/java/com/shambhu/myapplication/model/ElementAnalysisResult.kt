package com.shambhu.myapplication.model

data class ElementAnalysisResult(
    val dominantElement: String,
    val elementScores: Map<String, Double>
)

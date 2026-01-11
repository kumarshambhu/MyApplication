package com.shambhu.myapplication.model

data class ElementAnalysisResult(
    val dominantElementKey: String,
    val dominantExcessDescription: String,
    val dominantDefinitionDescription: String,
    val dominantDefinitionDetail: String,
    val elementScores: Map<String, Double>,
    val elementValueMatching: Map<String, List<Int>>,
    val elementLetterMatching: Map<String, List<String>>
)

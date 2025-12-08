package com.shambhu.myapplication.model

import java.io.Serializable

data class NumerologyMobileCombination(
    val combination: String,
    val state: String,
    val planets: List<String>,
    val traits: List<String>
) : Serializable

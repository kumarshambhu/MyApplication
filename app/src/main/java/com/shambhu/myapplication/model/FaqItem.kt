package com.shambhu.myapplication.model

data class FaqItem(
    val question: String,
    val answer: String,
    var isExpanded: Boolean = false
)

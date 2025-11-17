package com.shambhu.myapplication.model

data class KarmicAccordionItem(
    val header: String,
    val title: String,
    val content: String,
    var isExpanded: Boolean = false
)

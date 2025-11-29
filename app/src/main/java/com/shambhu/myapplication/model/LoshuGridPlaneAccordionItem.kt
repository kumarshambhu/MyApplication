package com.shambhu.myapplication.model

data class LoshuGridPlaneAccordionItem(
    val header: String,
    val presentNumber: String,
    val content: String,
    val imageSource: String,
    var isExpanded: Boolean = false
)

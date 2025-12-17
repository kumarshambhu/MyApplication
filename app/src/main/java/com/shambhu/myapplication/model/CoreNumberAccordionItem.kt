package com.shambhu.myapplication.model

data class CoreNumberAccordionItem(
    val header: String,
    val title: String,
    val numberDataModel: NameNumberDataModel,
    val imageSource: String,
    var isExpanded: Boolean = false
)

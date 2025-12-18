package com.shambhu.myapplication.model

data class FaqResponse(
    val allFaq: List<FaqDefinition>
)
data class FaqInnerItem(val key: String, val details: List<String>)
data class FaqDefinition(val name: String, val description: List<FaqInnerItem>, var isExpanded: Boolean)

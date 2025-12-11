package com.shambhu.myapplication.model

data class ElementData(
    val element: Map<String, List<ElementInfo>>,
    val definition: Map<String, ElementDefinition>,
    val excess: Map<String, ElementExcess>
)

data class ElementInfo(
    val quantity: Double,
    val element: String
)

data class ElementDefinition(
    val description: String,
    val details: String
)

data class ElementExcess(
    val details: String
)

package com.shambhu.myapplication.model

data class Plane(
    val id: Int,
    val name: String,
    val sections: List<Section>
)

data class Section(
    val title: String,
    val description: String,
    val traits: List<String>
)
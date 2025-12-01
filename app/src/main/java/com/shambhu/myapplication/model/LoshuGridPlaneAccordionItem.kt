package com.shambhu.myapplication.model

import androidx.annotation.DrawableRes

data class LoshuGridPlaneAccordionItem(
    val header: String,
    val presentNumber: String,
    val content: String,
    val imageSource: String,
    @DrawableRes val backgroundColor: Int,
    val headerColor: Int,
    var isExpanded: Boolean = false
)

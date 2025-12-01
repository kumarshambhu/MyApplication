package com.shambhu.myapplication.model

import androidx.annotation.DrawableRes

data class LoshuGridPlaneAccordionItem(
    val header: String,
    val presentNumber: String,
    val content: String,
    val imageSource: String,
    @DrawableRes val backgroundColor: Int,
    @DrawableRes val headerColor: Int,
    var isExpanded: Boolean = false
)

package com.shambhu.myapplication.model

import com.google.gson.annotations.SerializedName

data class ColorsData(
    @SerializedName("color_by_number")
    val colorByNumber: Map<String, ColorDetail>,
    @SerializedName("color_group")
    val colorGroup: Map<String, ColorGroup>
)

package com.shambhu.myapplication.model

import com.google.gson.annotations.SerializedName

data class LifeCycleDataResponse(
    @SerializedName("cycles_table")
    val cyclesTable: List<LifeCycleEntry>
)

data class LifeCycleEntry(
    @SerializedName("life_path_number")
    val lifePathNumber: String,
    @SerializedName("formative_cycle")
    val formativeCycle: String,
    @SerializedName("productive_cycle")
    val productiveCycle: String,
    @SerializedName("harvest_cycle")
    val harvestCycle: String
)

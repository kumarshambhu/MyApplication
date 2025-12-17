package com.shambhu.myapplication.model

data class NameNumberDataModel(val id: Int, val header: String, val details:List<NameNumberDetail>)
data class NameNumberDetail(val key: String, val details: String)

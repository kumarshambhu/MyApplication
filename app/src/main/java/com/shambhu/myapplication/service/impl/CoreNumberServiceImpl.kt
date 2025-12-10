package com.shambhu.myapplication.service.impl

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shambhu.myapplication.model.NumeroData
import com.shambhu.myapplication.service.CoreNumberService
import com.shambhu.myapplication.utils.CommonUtils


class CoreNumberServiceImpl(private val gson: Gson) : CoreNumberService {
    override suspend fun getMulankList(context: Context): List<NumeroData> {
        try {
            val jsonString = CommonUtils.readAssetFile(context, "mulank.json")
            val userListType = object : TypeToken<List<NumeroData>>() {}.type
            return  gson.fromJson(jsonString, userListType)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return emptyList()
    }

    override suspend fun getBhagyankList(context: Context): List<NumeroData> {
        try {
            val jsonString = CommonUtils.readAssetFile(context, "bhagyank.json")
            val userListType = object : TypeToken<List<NumeroData>>() {}.type
            return gson.fromJson(jsonString, userListType)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return emptyList()
    }
}






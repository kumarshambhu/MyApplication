package com.shambhu.myapplication.service.impl

import android.content.Context
import com.google.gson.Gson
import com.shambhu.myapplication.model.NumeroData
import com.shambhu.myapplication.service.CoreNumberService
import com.shambhu.myapplication.utils.CommonUtils
import org.json.JSONObject

class CoreNumberServiceImpl(private val gson: Gson) : CoreNumberService {
    override suspend fun getMulankById(context: Context, id: Int): NumeroData? {
        return try {
            val jsonString = CommonUtils.readAssetFile(context, "mulank.json")
            val jsonObject = JSONObject(jsonString)
            val mulankObject = jsonObject.getJSONObject("mulank_data").getJSONObject(id.toString())
            gson.fromJson(mulankObject.toString(), NumeroData::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun getBhagyankById(context: Context, id: Int): NumeroData? {
        return try {
            val jsonString = CommonUtils.readAssetFile(context, "bhagyank.json")
            val jsonObject = JSONObject(jsonString)
            val bhagyankObject =
                jsonObject.getJSONObject("bhagyank_numbers").getJSONObject(id.toString())
            gson.fromJson(bhagyankObject.toString(), NumeroData::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}






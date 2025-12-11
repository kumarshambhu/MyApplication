package com.shambhu.myapplication.service.impl

import android.content.Context
import android.util.Log
import com.google.gson.GsonBuilder
import com.shambhu.myapplication.model.MissingNumberData
import com.shambhu.myapplication.model.OccurrenceCount
import com.shambhu.myapplication.model.OccurrenceCountAdapter
import com.shambhu.myapplication.model.RepetitiveNumberData
import com.shambhu.myapplication.service.LoshuGridService
import com.shambhu.myapplication.utils.CommonUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

class LoshuGridServiceImpl(private val context: Context) : LoshuGridService {

    private val gson = GsonBuilder()
        .registerTypeAdapter(OccurrenceCount::class.java, OccurrenceCountAdapter())
        .create()

    override fun getMissingNumberData(): Flow<MissingNumberData> = flow {
        try {
            val json = CommonUtils.readAssetFile(context, "missing_number.json")
            val data = gson.fromJson(json, MissingNumberData::class.java)
            emit(data)
        } catch (e: IOException) {
            Log.e("LoshuGridService", "Error reading missing_number.json", e)
        }
    }

    override fun getRepetitiveNumberData(): Flow<RepetitiveNumberData> = flow {
        try {
            val json = CommonUtils.readAssetFile(context, "repeate_number.json")
            val data = gson.fromJson(json, RepetitiveNumberData::class.java)
            emit(data)
        } catch (e: IOException) {
            Log.e("LoshuGridService", "Error reading repeate_number.json", e)
        }
    }
}

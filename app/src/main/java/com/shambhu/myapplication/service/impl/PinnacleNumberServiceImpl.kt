package com.shambhu.myapplication.service.impl

import android.content.Context
import com.google.gson.Gson
import com.shambhu.myapplication.model.PinnacleNumberData
import com.shambhu.myapplication.service.PinnacleNumberService
import com.shambhu.myapplication.utils.CommonUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

class PinnacleNumberServiceImpl(private val context: Context) : PinnacleNumberService {
    override fun getPinnacleNumberData(): Flow<PinnacleNumberData> = flow {
        val jsonString = CommonUtils.readAssetFile(context, "pinnacle_number.json")
        val pinnacleNumberData = Gson().fromJson(jsonString, PinnacleNumberData::class.java)
        emit(pinnacleNumberData)
    }
}

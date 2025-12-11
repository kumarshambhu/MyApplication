package com.shambhu.myapplication.service.impl

import android.content.Context
import com.google.gson.Gson
import com.shambhu.myapplication.model.MaturityData
import com.shambhu.myapplication.model.MaturityDataResponse
import com.shambhu.myapplication.service.MaturityService
import com.shambhu.myapplication.utils.CommonUtils

class MaturityServiceImpl(private val gson: Gson) : MaturityService {
    override suspend fun getMaturityInterpretation(
        context: Context,
        maturityNumber: Int
    ): MaturityData? {
        val jsonString = CommonUtils.readAssetFile(context, "maturity.json")
        val maturityDataResponse = gson.fromJson(jsonString, MaturityDataResponse::class.java)
        return maturityDataResponse.maturityNumbers[maturityNumber.toString()]
    }
}

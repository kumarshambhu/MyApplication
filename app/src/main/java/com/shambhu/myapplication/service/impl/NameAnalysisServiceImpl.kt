package com.shambhu.myapplication.service.impl

import android.content.Context
import com.google.gson.Gson
import com.shambhu.myapplication.model.ColorAnalysisResult
import com.shambhu.myapplication.model.ElementAnalysisResult
import com.shambhu.myapplication.service.NameAnalysisService
import com.shambhu.myapplication.utils.CommonUtils
import com.shambhu.myapplication.utils.NumerologyCalculationUtils

class NameAnalysisServiceImpl(private val gson: Gson) : NameAnalysisService {
    override suspend fun getColorGroup(
        context: Context,
        fullName: String
    ): ColorAnalysisResult {
        val jsonString = CommonUtils.readAssetFile(context, "colors.json")
        return NumerologyCalculationUtils.calculateColorGroup(fullName, jsonString)
    }

    override suspend fun getElements(
        context: Context,
        fullName: String
    ): ElementAnalysisResult {
        val jsonString = CommonUtils.readAssetFile(context, "elements.json")
        return NumerologyCalculationUtils.calculateElements(fullName, jsonString)
    }
}

package com.shambhu.myapplication.service.impl

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shambhu.myapplication.model.BhagyankData
import com.shambhu.myapplication.model.MulankBhagyankResponse
import com.shambhu.myapplication.model.MulankData
import com.shambhu.myapplication.model.NumeroData
import com.shambhu.myapplication.model.NumeroData.Period
import com.shambhu.myapplication.model.SuccessNumberResponse
import com.shambhu.myapplication.service.CoreNumberService
import com.shambhu.myapplication.utils.CommonUtils
import org.json.JSONObject

class CoreNumberServiceImpl(private val gson: Gson) : CoreNumberService {
    override suspend fun getMulankById(context: Context, id: Int): NumeroData? {
        return try {
            val jsonString = CommonUtils.readAssetFile(context, "mulank.json")
            val listType = object : TypeToken<List<MulankData>>() {}.type
            val mulankList:List<MulankData> =  gson.fromJson(jsonString, listType)
            val mulankData: MulankData = mulankList.filter { it.id == id }.first()
            return mulankData.toEntity()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun getBhagyankById(context: Context, id: Int): NumeroData? {
        return try {
            val jsonString = CommonUtils.readAssetFile(context, "bhagyank.json")
            val listType = object : TypeToken<List<BhagyankData>>() {}.type
            val bhagyankList:List<BhagyankData> =  gson.fromJson(jsonString, listType)
            val bhagyankData: BhagyankData = bhagyankList.filter { it.id == id }.first()
            return bhagyankData.toEntity()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


}

private fun BhagyankData.toEntity(): NumeroData? {
    return NumeroData(
        id = this.id,
        name = this.name,
        rulingPlanet = this.rulingPlanet,
        birthDates = emptyList(),
        characteristics = emptyList(),
        strengths = emptyList(),
        weaknesses = emptyList(),
        advice = this.advice,
        favorablePeriods = emptyList(),
        unfavorablePeriods = emptyList(),
        luckyColors = emptyList(),
        colorUsageTips = emptyList(),
        traits = this.traits,
        careerSuggestions = this.careerSuggestions,
        genderSpecific = this.genderSpecific
    )
}
private fun MulankData.toEntity(): NumeroData {
    return NumeroData(
        id = this.id,
        name = this.name,
        rulingPlanet = this.rulingPlanet,
        birthDates = this.birthDates,
        characteristics = this.characteristics,
        strengths = this.strengths,
        weaknesses = this.weaknesses,
        advice = this.advice,
        favorablePeriods = this.favorablePeriods,
        unfavorablePeriods = this.unfavorablePeriods,
        luckyColors = this.luckyColors,
        colorUsageTips = this.colorUsageTips,
        traits = emptyList(),
        careerSuggestions = emptyList(),
        genderSpecific = emptyMap()
    )
}







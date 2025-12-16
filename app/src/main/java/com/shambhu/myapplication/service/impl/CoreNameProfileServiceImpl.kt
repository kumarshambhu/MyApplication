package com.shambhu.myapplication.service.impl

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shambhu.myapplication.R
import com.shambhu.myapplication.model.CoreNameDataModel
import com.shambhu.myapplication.model.CoreNumberAccordionItem
import com.shambhu.myapplication.model.NameNumberDataModel
import com.shambhu.myapplication.service.CoreNameProfileService
import com.shambhu.myapplication.utils.CommonUtils
import com.shambhu.myapplication.utils.NumerologyCalculationUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class CoreNameProfileServiceImpl : CoreNameProfileService {
    override fun getCoreNameProfileData(
        context: Context,
        fullName: String
    ): Flow<Pair<List<CoreNumberAccordionItem>, List<String>>> = flow {
        val soulNumberValue = NumerologyCalculationUtils.calculateSoulUrge(fullName)
        val personalityNumberValue = NumerologyCalculationUtils.calculatePersonality(fullName)
        val destinyNumberValue = NumerologyCalculationUtils.calculateExpression(fullName)

        val coreNumbers = CoreNameDataModel(soulNumberValue, personalityNumberValue, destinyNumberValue)

        val coreNumberItems = mutableListOf<CoreNumberAccordionItem>()
        val soulUrge = getData(context, "soul_urge.json", coreNumbers.soulUrgeNumber)
        val personality = getData(context, "personality.json", coreNumbers.personalityNumber)
        val destiny = getData(context, "destiny.json", coreNumbers.destinyNumber)

        val soulUrgeItem = CoreNumberAccordionItem(
            "${context.getString(R.string.soul_urge_number)} ${coreNumbers.soulUrgeNumber}",
            context.getString(R.string.soul_title),
            soulUrge,
            "ic_heart", false
        )
        val personalityItem = CoreNumberAccordionItem(
            "${context.getString(R.string.personality_number)} ${coreNumbers.personalityNumber}",
            context.getString(R.string.personality_title),
            personality,
            "ic_mirrors", false
        )
        val destinyItem = CoreNumberAccordionItem(
            "${context.getString(R.string.destiny_number)} ${coreNumbers.destinyNumber}",
            context.getString(R.string.destiny_title),
            destiny,
            "ic_mirrors", false
        )

        coreNumberItems.add(soulUrgeItem)
        coreNumberItems.add(personalityItem)
        coreNumberItems.add(destinyItem)

        val elementsJson = CommonUtils.readAssetFile(context, "combination.json")
        val combination = NumerologyCalculationUtils.calculateCombinationNameNumber(
            elementsJson,
            coreNumbers.destinyNumber,
            coreNumbers.soulUrgeNumber,
            coreNumbers.personalityNumber
        )
        val listType = object : TypeToken<List<String>>() {}.type
        val combinationList: List<String> = Gson().fromJson(combination, listType)

        emit(Pair(coreNumberItems, combinationList))
    }.flowOn(Dispatchers.IO)

    private fun getData(context: Context, jsonName: String, id: Int): NameNumberDataModel {
        val jsonString = CommonUtils.readAssetFile(context, jsonName)
        val listType = object : TypeToken<List<NameNumberDataModel>>() {}.type
        val dataList: List<NameNumberDataModel> = Gson().fromJson(jsonString, listType)
        return dataList.first { it.id == id }
    }
}
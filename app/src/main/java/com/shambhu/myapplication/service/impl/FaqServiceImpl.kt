package com.shambhu.myapplication.service.impl

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shambhu.myapplication.model.FaqDefinition
import com.shambhu.myapplication.service.FaqService
import com.shambhu.myapplication.utils.CommonUtils

class FaqServiceImpl(private val context: Context): FaqService {
    override fun getNumerologyDefinitions(context: Context): List<FaqDefinition>? {
        return try {
            val faqJson = CommonUtils.readAssetFile(context, "numerology_definition.json")
            val listType = object : TypeToken<List<FaqDefinition>>() {}.type
            return Gson().fromJson(faqJson, listType)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
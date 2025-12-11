package com.shambhu.myapplication.service.impl

import android.content.Context
import com.google.gson.Gson
import com.shambhu.myapplication.model.KarmicDebt
import com.shambhu.myapplication.model.KarmicDebtResponse
import com.shambhu.myapplication.model.KarmicLessonItem
import com.shambhu.myapplication.service.KarmicAnalysisService
import com.shambhu.myapplication.utils.CommonUtils
import com.shambhu.myapplication.utils.NumerologyCalculationUtils

class KarmicAnalysisServiceImpl(private val gson: Gson) : KarmicAnalysisService {
    override suspend fun getKarmicLessons(
        context: Context,
        fullName: String
    ): List<KarmicLessonItem> {
        return NumerologyCalculationUtils.calculateKarmicFromName(context, fullName)
    }

    override suspend fun getKarmicDebts(
        context: Context,
        day: Int,
        month: Int,
        year: Int,
        fullName: String
    ): List<KarmicDebt> {
        val karmicDebtNumbers =
            NumerologyCalculationUtils.calculateKarmicDebtNumbers(day, month, year, fullName)

        val karmicDebtJson = CommonUtils.readAssetFile(context, "karmic_debt.json")
        val karmicDebtData = gson.fromJson(karmicDebtJson, KarmicDebtResponse::class.java)
        val interpretations = karmicDebtData.karmic_debt

        return karmicDebtNumbers.map { karmicDebtItem ->
            val interpretation = interpretations[karmicDebtItem.number.toString()]
            KarmicDebt(
                number = karmicDebtItem.number.toString(),
                source = karmicDebtItem.source,
                challengesAndProblems = interpretation?.challengesAndProblems,
                qualities = interpretation?.qualities,
                keysToOvercome = interpretation?.keysToOvercome,
                summary = interpretation?.summary,
                potentialOutcome = interpretation?.potentialOutcome,
                isExpanded = false
            )
        }
    }
}

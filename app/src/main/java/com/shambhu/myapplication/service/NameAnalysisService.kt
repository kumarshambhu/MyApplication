package com.shambhu.myapplication.service

import android.content.Context
import com.shambhu.myapplication.utils.NumerologyCalculationUtils

interface NameAnalysisService {
    suspend fun getColorGroup(
        context: Context,
        fullName: String
    ): NumerologyCalculationUtils.Quintuple<String, String, String, String, Int>

    suspend fun getElements(
        context: Context,
        fullName: String
    ): Pair<String, Map<String, Double>>
}

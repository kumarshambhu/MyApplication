package com.shambhu.myapplication.repository

import android.content.Context
import com.shambhu.myapplication.utils.NumerologyCalculationUtils
import kotlinx.coroutines.flow.Flow

interface NameAnalysisRepository {
    fun getColorGroup(
        context: Context,
        fullName: String
    ): Flow<Result<NumerologyCalculationUtils.Quintuple<String, String, String, String, Int>>>

    fun getElements(
        context: Context,
        fullName: String
    ): Flow<Result<Pair<String, Map<String, Double>>>>
}

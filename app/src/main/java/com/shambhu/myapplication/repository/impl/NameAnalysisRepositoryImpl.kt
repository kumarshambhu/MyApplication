package com.shambhu.myapplication.repository.impl

import android.content.Context
import com.shambhu.myapplication.repository.NameAnalysisRepository
import com.shambhu.myapplication.service.NameAnalysisService
import com.shambhu.myapplication.utils.NumerologyCalculationUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NameAnalysisRepositoryImpl(private val nameAnalysisService: NameAnalysisService) :
    NameAnalysisRepository {
    override fun getColorGroup(
        context: Context,
        fullName: String
    ): Flow<Result<NumerologyCalculationUtils.Quintuple<String, String, String, String, Int>>> = flow {
        try {
            val colorGroup = nameAnalysisService.getColorGroup(context, fullName)
            emit(Result.success(colorGroup))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override fun getElements(
        context: Context,
        fullName: String
    ): Flow<Result<Pair<String, Map<String, Double>>>> = flow {
        try {
            val elements = nameAnalysisService.getElements(context, fullName)
            emit(Result.success(elements))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}

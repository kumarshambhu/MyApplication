package com.shambhu.myapplication.repository.impl

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.shambhu.myapplication.model.ColorAnalysisResult
import com.shambhu.myapplication.model.ElementAnalysisResult
import com.shambhu.myapplication.repository.NameAnalysisRepository
import com.shambhu.myapplication.service.NameAnalysisService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NameAnalysisRepositoryImpl(private val nameAnalysisService: NameAnalysisService) :
    NameAnalysisRepository {
    override fun getColorGroup(
        context: Context,
        fullName: String
    ): Flow<Result<ColorAnalysisResult>> = flow {
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
    ): Flow<Result<ElementAnalysisResult>> = flow {
        try {
            val elements = nameAnalysisService.getElements(context, fullName)
            Log.d("Element", Gson().toJson(elements))
            emit(Result.success(elements))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}

package com.shambhu.myapplication.repository

import android.content.Context
import com.shambhu.myapplication.model.ColorAnalysisResult
import com.shambhu.myapplication.model.ElementAnalysisResult
import kotlinx.coroutines.flow.Flow

interface NameAnalysisRepository {
    fun getColorGroup(
        context: Context,
        fullName: String
    ): Flow<Result<ColorAnalysisResult>>

    fun getElements(
        context: Context,
        fullName: String
    ): Flow<Result<ElementAnalysisResult>>
}

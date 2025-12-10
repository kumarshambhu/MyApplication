package com.shambhu.myapplication.service

import android.content.Context
import com.shambhu.myapplication.model.ColorAnalysisResult
import com.shambhu.myapplication.model.ElementAnalysisResult

interface NameAnalysisService {
    suspend fun getColorGroup(
        context: Context,
        fullName: String
    ): ColorAnalysisResult

    suspend fun getElements(
        context: Context,
        fullName: String
    ): ElementAnalysisResult
}

package com.shambhu.myapplication.service

import android.content.Context
import com.shambhu.myapplication.model.KarmicDebt
import com.shambhu.myapplication.model.KarmicLessonItem

interface KarmicAnalysisService {
    suspend fun getKarmicLessons(context: Context, fullName: String): List<KarmicLessonItem>
    suspend fun getKarmicDebts(context: Context, day: Int, month: Int, year: Int, fullName: String): List<KarmicDebt>
}

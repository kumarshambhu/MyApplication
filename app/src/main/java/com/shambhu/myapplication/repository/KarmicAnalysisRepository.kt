package com.shambhu.myapplication.repository

import android.content.Context
import com.shambhu.myapplication.model.KarmicDebt
import com.shambhu.myapplication.model.KarmicLessonItem
import kotlinx.coroutines.flow.Flow

interface KarmicAnalysisRepository {
    fun getKarmicLessons(context: Context, fullName: String): Flow<Result<List<KarmicLessonItem>>>
    fun getKarmicDebts(context: Context, day: Int, month: Int, year: Int, fullName: String): Flow<Result<List<KarmicDebt>>>
}

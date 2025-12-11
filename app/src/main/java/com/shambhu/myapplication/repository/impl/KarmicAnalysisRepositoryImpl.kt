package com.shambhu.myapplication.repository.impl

import android.content.Context
import com.shambhu.myapplication.model.KarmicDebt
import com.shambhu.myapplication.model.KarmicLessonItem
import com.shambhu.myapplication.repository.KarmicAnalysisRepository
import com.shambhu.myapplication.service.KarmicAnalysisService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class KarmicAnalysisRepositoryImpl(
    private val karmicAnalysisService: KarmicAnalysisService
) : KarmicAnalysisRepository {
    override fun getKarmicLessons(
        context: Context,
        fullName: String
    ): Flow<Result<List<KarmicLessonItem>>> = flow {
        val result = withContext(Dispatchers.IO) {
            karmicAnalysisService.getKarmicLessons(context, fullName)
        }
        emit(Result.success(result))
    }.catch {
        emit(Result.failure(it))
    }.flowOn(Dispatchers.Main)

    override fun getKarmicDebts(
        context: Context,
        day: Int,
        month: Int,
        year: Int,
        fullName: String
    ): Flow<Result<List<KarmicDebt>>> = flow {
        val result = withContext(Dispatchers.IO) {
            karmicAnalysisService.getKarmicDebts(context, day, month, year, fullName)
        }
        emit(Result.success(result))
    }.catch {
        emit(Result.failure(it))
    }.flowOn(Dispatchers.Main)
}

package com.shambhu.myapplication.repository.impl

import android.content.Context
import com.shambhu.myapplication.model.MaturityData
import com.shambhu.myapplication.repository.MaturityRepository
import com.shambhu.myapplication.service.MaturityService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class MaturityRepositoryImpl(
    private val maturityService: MaturityService
) : MaturityRepository {
    override fun getMaturityInterpretation(
        context: Context,
        maturityNumber: Int
    ): Flow<Result<MaturityData?>> = flow {
        val result = withContext(Dispatchers.IO) {
            maturityService.getMaturityInterpretation(context, maturityNumber)
        }
        emit(Result.success(result))
    }.catch {
        emit(Result.failure(it))
    }.flowOn(Dispatchers.Main)
}

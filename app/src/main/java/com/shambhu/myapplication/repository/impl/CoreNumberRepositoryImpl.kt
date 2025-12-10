package com.shambhu.myapplication.repository.impl

import android.content.Context
import com.shambhu.myapplication.model.NumeroData
import com.shambhu.myapplication.repository.CoreNumberRepository
import com.shambhu.myapplication.service.CoreNumberService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CoreNumberRepositoryImpl(private val coreNumberService: CoreNumberService) :
    CoreNumberRepository {
    override fun getMulankdataById(
        context: Context,
        userId: Int
    ): Flow<Result<NumeroData?>> = flow {
        try {
            val users = coreNumberService.getMulankList(context)
            val user = users.find { it.id == userId }
            emit(Result.success(user))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override fun getBhagyankById(
        context: Context,
        userId: Int
    ): Flow<Result<NumeroData?>> = flow {
        try {
            val users = coreNumberService.getBhagyankList(context)
            val user = users.find { it.id == userId }
            emit(Result.success(user))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}
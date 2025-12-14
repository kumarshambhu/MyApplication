package com.shambhu.myapplication.repository.impl

import android.content.Context
import com.shambhu.myapplication.model.ChallengeNumberData
import com.shambhu.myapplication.model.MaturityData
import com.shambhu.myapplication.model.PinnacleNumberData
import com.shambhu.myapplication.model.SuccessNumberResponse
import com.shambhu.myapplication.repository.MilestoneSuiteRepository
import com.shambhu.myapplication.service.MilestoneSuiteService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class MilestoneSuiteRepositoryImpl(private val milestoneSuiteService: MilestoneSuiteService) : MilestoneSuiteRepository {
    override fun getChallengeNumberData(): Flow<ChallengeNumberData> {
        return milestoneSuiteService.getChallengeNumberData()
    }

    override fun getPinnacleNumberData(): Flow<PinnacleNumberData> {
        return milestoneSuiteService.getPinnacleNumberData()
    }

    override fun getMaturityInterpretation(
        context: Context,
        maturityNumber: Int
    ): Flow<Result<MaturityData?>> = flow {
        val result = withContext(Dispatchers.IO) {
            milestoneSuiteService.getMaturityInterpretation(context, maturityNumber)
        }
        emit(Result.success(result))
    }.catch {
        emit(Result.failure(it))
    }.flowOn(Dispatchers.Main)


    override fun getSuccessNumberData(): Flow<SuccessNumberResponse> {
        return milestoneSuiteService.getSuccessNumberData()
    }
}

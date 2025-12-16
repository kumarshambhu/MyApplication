package com.shambhu.myapplication.service

import android.content.Context
import com.shambhu.myapplication.model.CareerData
import com.shambhu.myapplication.model.ChallengeNumberData
import com.shambhu.myapplication.model.MaturityData
import com.shambhu.myapplication.model.PinnacleNumberData
import com.shambhu.myapplication.model.SuccessNumberResponse
import kotlinx.coroutines.flow.Flow

interface MilestoneSuiteService {
    fun getChallengeNumberData(): Flow<ChallengeNumberData>
    fun getPinnacleNumberData(): Flow<PinnacleNumberData>
    suspend fun getMaturityInterpretation(context: Context, maturityNumber: Int): MaturityData?
    fun getSuccessNumberData(): Flow<SuccessNumberResponse>
    fun getCareers(number: Int): Flow<CareerData>
}

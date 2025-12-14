package com.shambhu.myapplication.service

import android.content.Context
import com.shambhu.myapplication.model.ChallengeNumberData
import com.shambhu.myapplication.model.MaturityData
import com.shambhu.myapplication.model.PinnacleNumberData
import kotlinx.coroutines.flow.Flow

interface MilestoneSuiteService {
    fun getChallengeNumberData(): Flow<ChallengeNumberData>
    fun getPinnacleNumberData(): Flow<PinnacleNumberData>
    suspend fun getMaturityInterpretation(context: Context, maturityNumber: Int): MaturityData?
}

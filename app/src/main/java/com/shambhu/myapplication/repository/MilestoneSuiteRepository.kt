package com.shambhu.myapplication.repository

import android.content.Context
import com.shambhu.myapplication.model.ChallengeNumberData
import com.shambhu.myapplication.model.MaturityData
import com.shambhu.myapplication.model.PinnacleNumberData
import kotlinx.coroutines.flow.Flow

interface MilestoneSuiteRepository {
    fun getChallengeNumberData(): Flow<ChallengeNumberData>
    fun getPinnacleNumberData(): Flow<PinnacleNumberData>
    fun getMaturityInterpretation(context: Context, maturityNumber: Int): Flow<Result<MaturityData?>>
}

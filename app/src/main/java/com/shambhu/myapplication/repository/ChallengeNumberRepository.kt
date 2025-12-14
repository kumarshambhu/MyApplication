package com.shambhu.myapplication.repository

import com.shambhu.myapplication.model.ChallengeNumberData
import kotlinx.coroutines.flow.Flow

interface ChallengeNumberRepository {
    fun getChallengeNumberData(): Flow<ChallengeNumberData>
}

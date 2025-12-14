package com.shambhu.myapplication.service

import com.shambhu.myapplication.model.ChallengeNumberData
import kotlinx.coroutines.flow.Flow

interface ChallengeNumberService {
    fun getChallengeNumberData(): Flow<ChallengeNumberData>
}

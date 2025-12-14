package com.shambhu.myapplication.repository.impl

import com.shambhu.myapplication.model.ChallengeNumberData
import com.shambhu.myapplication.repository.ChallengeNumberRepository
import com.shambhu.myapplication.service.ChallengeNumberService
import kotlinx.coroutines.flow.Flow

class ChallengeNumberRepositoryImpl(private val challengeNumberService: ChallengeNumberService) : ChallengeNumberRepository {
    override fun getChallengeNumberData(): Flow<ChallengeNumberData> {
        return challengeNumberService.getChallengeNumberData()
    }
}

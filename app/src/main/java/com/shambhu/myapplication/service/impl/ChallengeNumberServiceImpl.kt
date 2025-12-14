package com.shambhu.myapplication.service.impl

import android.content.Context
import com.google.gson.Gson
import com.shambhu.myapplication.model.ChallengeNumberData
import com.shambhu.myapplication.service.ChallengeNumberService
import com.shambhu.myapplication.utils.CommonUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

class ChallengeNumberServiceImpl(private val context: Context) : ChallengeNumberService {
    override fun getChallengeNumberData(): Flow<ChallengeNumberData> = flow {
        val jsonString = CommonUtils.readAssetFile(context, "challenge_number.json")
        val challengeNumberData = Gson().fromJson(jsonString, ChallengeNumberData::class.java)
        emit(challengeNumberData)
    }
}

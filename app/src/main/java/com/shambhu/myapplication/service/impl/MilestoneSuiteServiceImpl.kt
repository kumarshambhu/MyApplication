package com.shambhu.myapplication.service.impl

import android.content.Context
import com.google.gson.Gson
import com.shambhu.myapplication.model.CareerData
import com.shambhu.myapplication.model.ChallengeNumberData
import com.shambhu.myapplication.model.MaturityData
import com.shambhu.myapplication.model.MaturityDataResponse
import com.shambhu.myapplication.model.PinnacleNumberData
import com.shambhu.myapplication.model.SuccessNumberResponse
import com.shambhu.myapplication.service.MilestoneSuiteService
import com.shambhu.myapplication.utils.CommonUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.json.JSONObject

class MilestoneSuiteServiceImpl(private val context: Context) : MilestoneSuiteService {
    override fun getChallengeNumberData(): Flow<ChallengeNumberData> = flow {
        val jsonString = CommonUtils.readAssetFile(context, "challenge_number.json")
        val challengeNumberData = Gson().fromJson(jsonString, ChallengeNumberData::class.java)
        emit(challengeNumberData)
    }

    override fun getPinnacleNumberData(): Flow<PinnacleNumberData> = flow {
        val jsonString = CommonUtils.readAssetFile(context, "pinnacle_number.json")
        val pinnacleNumberData = Gson().fromJson(jsonString, PinnacleNumberData::class.java)
        emit(pinnacleNumberData)
    }

    override suspend fun getMaturityInterpretation(
        context: Context,
        maturityNumber: Int
    ): MaturityData? {
        val jsonString = CommonUtils.readAssetFile(context, "maturity.json")
        val maturityDataResponse = Gson().fromJson(jsonString, MaturityDataResponse::class.java)
        return maturityDataResponse.maturityNumbers[maturityNumber.toString()]
    }

    override fun getSuccessNumberData(): Flow<SuccessNumberResponse> = flow {
        val jsonString = CommonUtils.readAssetFile(context, "success_number.json")
        val successNumberData = Gson().fromJson(jsonString, SuccessNumberResponse::class.java)
        emit(successNumberData)
    }.flowOn(Dispatchers.IO)


    override fun getCareers(number: Int): Flow<CareerData> = flow {
        val json = context.assets.open("careers.json").bufferedReader().use { it.readText() }
        val jsonObject = JSONObject(json)
        val numbers = JSONObject(jsonObject.get("numbers").toString())

        val careersArray = numbers.getJSONArray(number.toString())
        val careers = mutableListOf<String>()
        for (i in 0 until careersArray.length()) {
            careers.add(careersArray.getString(i))
        }
        emit(CareerData(careers))
    }.flowOn(Dispatchers.IO)
}

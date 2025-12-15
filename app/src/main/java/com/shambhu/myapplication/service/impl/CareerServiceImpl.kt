package com.shambhu.myapplication.service.impl

import android.content.Context
import com.shambhu.myapplication.model.CareerData
import com.shambhu.myapplication.service.CareerService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.json.JSONObject

class CareerServiceImpl(private val context: Context) : CareerService {

    override fun getCareers(number: Int): Flow<CareerData> = flow {
        val json = context.assets.open("careers.json").bufferedReader().use { it.readText() }
        val jsonObject = JSONObject(json)
        val careersArray = jsonObject.getJSONArray(number.toString())
        val careers = mutableListOf<String>()
        for (i in 0 until careersArray.length()) {
            careers.add(careersArray.getString(i))
        }
        emit(CareerData(careers))
    }.flowOn(Dispatchers.IO)
}

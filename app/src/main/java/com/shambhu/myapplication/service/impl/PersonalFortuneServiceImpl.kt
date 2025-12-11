package com.shambhu.myapplication.service.impl

import android.content.Context
import com.google.gson.Gson
import com.shambhu.myapplication.model.PersonalDay
import com.shambhu.myapplication.model.PersonalDayResponse
import com.shambhu.myapplication.model.PersonalMonth
import com.shambhu.myapplication.model.PersonalMonthResponse
import com.shambhu.myapplication.model.PersonalYear
import com.shambhu.myapplication.model.PersonalYearResponse
import com.shambhu.myapplication.service.PersonalFortuneService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException

class PersonalFortuneServiceImpl(private val context: Context) : PersonalFortuneService {

    private val gson = Gson()

    override fun getPersonalDay(dayNumber: Int): Flow<PersonalDay?> = flow {
        try {
            val json = context.assets.open("personal_day.json").bufferedReader().use { it.readText() }
            val response = gson.fromJson(json, PersonalDayResponse::class.java)
            val personalDay = response.personalDays.find { it.dayNumber == dayNumber }
            emit(personalDay)
        } catch (e: IOException) {
            emit(null)
        }
    }.flowOn(Dispatchers.IO)

    override fun getPersonalMonth(monthNumber: Int): Flow<PersonalMonth?> = flow {
        try {
            val json = context.assets.open("personal_month.json").bufferedReader().use { it.readText() }
            val response = gson.fromJson(json, PersonalMonthResponse::class.java)
            val personalMonth = response.personalMonths.find { it.monthNumber == monthNumber }
            emit(personalMonth)
        } catch (e: IOException) {
            emit(null)
        }
    }.flowOn(Dispatchers.IO)

    override fun getPersonalYear(yearNumber: Int): Flow<PersonalYear?> = flow {
        try {
            val json = context.assets.open("personal_year.json").bufferedReader().use { it.readText() }
            val response = gson.fromJson(json, PersonalYearResponse::class.java)
            val personalYear = response.personalYears.find { it.yearNumber == yearNumber }
            emit(personalYear)
        } catch (e: IOException) {
            emit(null)
        }
    }.flowOn(Dispatchers.IO)
}

package com.shambhu.myapplication.service

import com.shambhu.myapplication.model.PersonalDay
import com.shambhu.myapplication.model.PersonalMonth
import com.shambhu.myapplication.model.PersonalYear
import kotlinx.coroutines.flow.Flow

interface PersonalFortuneService {
    fun getPersonalDay(dayNumber: Int): Flow<PersonalDay?>
    fun getPersonalMonth(monthNumber: Int): Flow<PersonalMonth?>
    fun getPersonalYear(yearNumber: Int): Flow<PersonalYear?>
}

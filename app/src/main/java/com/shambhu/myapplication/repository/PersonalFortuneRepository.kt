package com.shambhu.myapplication.repository

import com.shambhu.myapplication.model.PersonalFortuneData
import kotlinx.coroutines.flow.Flow

interface PersonalFortuneRepository {
    fun getPersonalFortune(
        dayNumber: Int,
        monthNumber: Int,
        yearNumber: Int
    ): Flow<PersonalFortuneData>
}

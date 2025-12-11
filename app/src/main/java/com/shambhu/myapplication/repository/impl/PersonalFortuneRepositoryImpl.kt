package com.shambhu.myapplication.repository.impl

import com.shambhu.myapplication.model.PersonalFortuneData
import com.shambhu.myapplication.repository.PersonalFortuneRepository
import com.shambhu.myapplication.service.PersonalFortuneService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.zip

class PersonalFortuneRepositoryImpl(
    private val personalFortuneService: PersonalFortuneService
) : PersonalFortuneRepository {
    override fun getPersonalFortune(
        dayNumber: Int,
        monthNumber: Int,
        yearNumber: Int
    ): Flow<PersonalFortuneData> {
        return personalFortuneService.getPersonalDay(dayNumber)
            .zip(personalFortuneService.getPersonalMonth(monthNumber)) { personalDay, personalMonth ->
                personalDay to personalMonth
            }.zip(personalFortuneService.getPersonalYear(yearNumber)) { (personalDay, personalMonth), personalYear ->
                PersonalFortuneData(personalDay, personalMonth, personalYear)
            }
    }
}

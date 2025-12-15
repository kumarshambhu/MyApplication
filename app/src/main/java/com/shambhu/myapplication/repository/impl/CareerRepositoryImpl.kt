package com.shambhu.myapplication.repository.impl

import com.shambhu.myapplication.model.CareerData
import com.shambhu.myapplication.repository.CareerRepository
import com.shambhu.myapplication.service.CareerService
import kotlinx.coroutines.flow.Flow

class CareerRepositoryImpl(private val careerService: CareerService) : CareerRepository {

    override fun getCareers(number: Int): Flow<CareerData> {
        return careerService.getCareers(number)
    }
}

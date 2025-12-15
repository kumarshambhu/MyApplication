package com.shambhu.myapplication.service

import com.shambhu.myapplication.model.CareerData
import kotlinx.coroutines.flow.Flow

interface CareerService {
    fun getCareers(number: Int): Flow<CareerData>
}

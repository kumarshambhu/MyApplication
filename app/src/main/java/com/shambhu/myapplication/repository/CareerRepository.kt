package com.shambhu.myapplication.repository

import com.shambhu.myapplication.model.CareerData
import kotlinx.coroutines.flow.Flow

interface CareerRepository {
    fun getCareers(number: Int): Flow<CareerData>
}

package com.shambhu.myapplication.service

import com.shambhu.myapplication.model.PinnacleNumberData
import kotlinx.coroutines.flow.Flow

interface PinnacleNumberService {
    fun getPinnacleNumberData(): Flow<PinnacleNumberData>
}

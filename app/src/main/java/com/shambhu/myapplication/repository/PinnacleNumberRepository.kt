package com.shambhu.myapplication.repository

import com.shambhu.myapplication.model.PinnacleNumberData
import kotlinx.coroutines.flow.Flow

interface PinnacleNumberRepository {
    fun getPinnacleNumberData(): Flow<PinnacleNumberData>
}

package com.shambhu.myapplication.repository.impl

import com.shambhu.myapplication.model.PinnacleNumberData
import com.shambhu.myapplication.repository.PinnacleNumberRepository
import com.shambhu.myapplication.service.PinnacleNumberService
import kotlinx.coroutines.flow.Flow

class PinnacleNumberRepositoryImpl(private val pinnacleNumberService: PinnacleNumberService) : PinnacleNumberRepository {
    override fun getPinnacleNumberData(): Flow<PinnacleNumberData> {
        return pinnacleNumberService.getPinnacleNumberData()
    }
}

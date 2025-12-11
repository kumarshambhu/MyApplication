package com.shambhu.myapplication.repository.impl

import com.shambhu.myapplication.model.MissingNumberData
import com.shambhu.myapplication.model.RepetitiveNumberData
import com.shambhu.myapplication.repository.LoshuGridRepository
import com.shambhu.myapplication.service.LoshuGridService
import kotlinx.coroutines.flow.Flow

class LoshuGridRepositoryImpl(private val loshuGridService: LoshuGridService) : LoshuGridRepository {

    override fun getMissingNumberData(): Flow<MissingNumberData> {
        return loshuGridService.getMissingNumberData()
    }

    override fun getRepetitiveNumberData(): Flow<RepetitiveNumberData> {
        return loshuGridService.getRepetitiveNumberData()
    }
}

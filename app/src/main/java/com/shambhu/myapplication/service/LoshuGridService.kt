package com.shambhu.myapplication.service

import com.shambhu.myapplication.model.MissingNumberData
import com.shambhu.myapplication.model.RepetitiveNumberData
import kotlinx.coroutines.flow.Flow

interface LoshuGridService {
    fun getMissingNumberData(): Flow<MissingNumberData>
    fun getRepetitiveNumberData(): Flow<RepetitiveNumberData>
}

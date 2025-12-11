package com.shambhu.myapplication.repository

import com.shambhu.myapplication.model.MissingNumberData
import com.shambhu.myapplication.model.RepetitiveNumberData
import kotlinx.coroutines.flow.Flow

interface LoshuGridRepository {
    fun getMissingNumberData(): Flow<MissingNumberData>
    fun getRepetitiveNumberData(): Flow<RepetitiveNumberData>
}

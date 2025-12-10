package com.shambhu.myapplication.repository

import android.content.Context
import com.shambhu.myapplication.model.NumeroData
import kotlinx.coroutines.flow.Flow

interface CoreNumberRepository {
    suspend fun getMulankdataById(context: Context, userId: Int): Flow<Result<NumeroData?>>
}
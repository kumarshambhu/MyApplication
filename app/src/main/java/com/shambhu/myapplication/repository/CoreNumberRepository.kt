package com.shambhu.myapplication.repository

import android.content.Context
import com.shambhu.myapplication.model.NumeroData
import kotlinx.coroutines.flow.Flow

interface CoreNumberRepository {
    fun getMulankdataById(context: Context, id: Int): Flow<Result<NumeroData?>>
    fun getBhagyankById(context: Context, id: Int): Flow<Result<NumeroData?>>
}
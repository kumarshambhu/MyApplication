package com.shambhu.myapplication.repository

import android.content.Context
import com.shambhu.myapplication.model.MaturityData
import kotlinx.coroutines.flow.Flow

interface MaturityRepository {
    fun getMaturityInterpretation(context: Context, maturityNumber: Int): Flow<Result<MaturityData?>>
}

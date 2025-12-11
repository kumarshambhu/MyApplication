package com.shambhu.myapplication.service

import android.content.Context
import com.shambhu.myapplication.model.MaturityData

interface MaturityService {
    suspend fun getMaturityInterpretation(context: Context, maturityNumber: Int): MaturityData?
}

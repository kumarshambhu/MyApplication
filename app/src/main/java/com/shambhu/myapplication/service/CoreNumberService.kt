package com.shambhu.myapplication.service

import android.content.Context
import com.shambhu.myapplication.model.NumeroData

interface CoreNumberService {
    suspend fun getMulankById(context: Context, id: Int): NumeroData?
    suspend fun getBhagyankById(context: Context, id: Int): NumeroData?
}
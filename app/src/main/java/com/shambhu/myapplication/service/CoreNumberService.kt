package com.shambhu.myapplication.service

import android.content.Context
import com.shambhu.myapplication.model.NumeroData

interface  CoreNumberService {
    suspend fun getMulankList(context: Context): List<NumeroData>
    suspend fun getBhagyankList(context: Context): List<NumeroData>

}
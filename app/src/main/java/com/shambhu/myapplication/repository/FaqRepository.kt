package com.shambhu.myapplication.repository

import android.content.Context
import com.shambhu.myapplication.model.FaqItem
import kotlinx.coroutines.flow.Flow

interface FaqRepository {
    fun getFaqItems(context: Context): Flow<List<FaqItem>>
}

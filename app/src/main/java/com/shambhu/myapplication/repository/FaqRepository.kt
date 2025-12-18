package com.shambhu.myapplication.repository

import android.content.Context
import com.shambhu.myapplication.model.FaqDefinition
import kotlinx.coroutines.flow.Flow

interface FaqRepository {
    fun getFaqItems(context: Context): Flow<List<FaqDefinition>>
}

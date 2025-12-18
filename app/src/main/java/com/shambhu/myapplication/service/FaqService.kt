package com.shambhu.myapplication.service

import android.content.Context
import com.shambhu.myapplication.model.FaqDefinition
import kotlinx.coroutines.flow.Flow

interface FaqService {
    fun getNumerologyDefinitions(context: android.content.Context): List<FaqDefinition>?
}
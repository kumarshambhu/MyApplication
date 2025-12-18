package com.shambhu.myapplication.repository.impl

import android.content.Context
import com.shambhu.myapplication.model.FaqDefinition
import com.shambhu.myapplication.repository.FaqRepository
import com.shambhu.myapplication.service.FaqService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FaqRepositoryImpl(private val faqService: FaqService) : FaqRepository {
    override fun getFaqItems(context: Context): Flow<List<FaqDefinition>> = flow {
        val elements: List<FaqDefinition>? = faqService.getNumerologyDefinitions(context)
        elements?.let { emit(it) }
    }
}

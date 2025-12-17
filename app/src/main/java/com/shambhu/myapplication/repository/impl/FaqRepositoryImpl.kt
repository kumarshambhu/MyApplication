package com.shambhu.myapplication.repository.impl

import android.content.Context
import com.shambhu.myapplication.model.FaqItem
import com.shambhu.myapplication.repository.FaqRepository
import com.shambhu.myapplication.service.FaqService
import com.shambhu.myapplication.service.impl.FaqServiceImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FaqRepositoryImpl(private val faqService: FaqService = FaqServiceImpl()) : FaqRepository {
    override fun getFaqItems(context: Context): Flow<List<FaqItem>> {
        return faqService.getNumerologyDefinitions(context).map { definitions ->
            definitions.map { definition ->
                val answer = definition.description.joinToString("\n\n") { detail ->
                    "${detail.key}\n${detail.details.joinToString("\n")}"
                }
                FaqItem(question = definition.name, answer = answer)
            }
        }
    }
}

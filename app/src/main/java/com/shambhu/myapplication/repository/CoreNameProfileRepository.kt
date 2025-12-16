package com.shambhu.myapplication.repository

import android.content.Context
import com.shambhu.myapplication.model.CoreNumberAccordionItem
import kotlinx.coroutines.flow.Flow

interface CoreNameProfileRepository {
    fun getCoreNameProfileData(
        context: Context,
        fullName: String
    ): Flow<Pair<List<CoreNumberAccordionItem>, List<String>>>
}

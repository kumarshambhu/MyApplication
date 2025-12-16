package com.shambhu.myapplication.service

import android.content.Context
import com.shambhu.myapplication.model.CoreNameDataModel
import com.shambhu.myapplication.model.CoreNumberAccordionItem
import com.shambhu.myapplication.model.NameNumberDataModel
import kotlinx.coroutines.flow.Flow

interface CoreNameProfileService {
    fun getCoreNameProfileData(
        context: Context,
        fullName: String
    ): Flow<Pair<List<CoreNumberAccordionItem>, List<String>>>
}
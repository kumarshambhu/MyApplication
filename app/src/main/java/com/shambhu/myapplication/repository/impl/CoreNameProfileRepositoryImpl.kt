package com.shambhu.myapplication.repository.impl

import android.content.Context
import com.shambhu.myapplication.model.CoreNumberAccordionItem
import com.shambhu.myapplication.repository.CoreNameProfileRepository
import com.shambhu.myapplication.service.CoreNameProfileService
import kotlinx.coroutines.flow.Flow

class CoreNameProfileRepositoryImpl(private val coreNameProfileService: CoreNameProfileService) :
    CoreNameProfileRepository {
    override fun getCoreNameProfileData(
        context: Context,
        fullName: String
    ): Flow<Pair<List<CoreNumberAccordionItem>, List<String>>> {
        return coreNameProfileService.getCoreNameProfileData(context, fullName)
    }
}

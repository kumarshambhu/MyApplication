package com.shambhu.myapplication.di


import com.google.gson.Gson
import com.shambhu.myapplication.repository.CoreNumberRepository
import com.shambhu.myapplication.service.CoreNumberService
import com.shambhu.myapplication.repository.impl.CoreNumberRepositoryImpl
import com.shambhu.myapplication.service.impl.CoreNumberServiceImpl
import org.koin.dsl.module

val appModule = module {
    single { Gson() }
    single<CoreNumberService> { CoreNumberServiceImpl(get()) }
    single<CoreNumberRepository> { CoreNumberRepositoryImpl(get()) }
}
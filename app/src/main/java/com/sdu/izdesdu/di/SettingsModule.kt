package com.sdu.izdesdu.di

import com.sdu.izdesdu.data.local.SettingsDataStore
import com.sdu.izdesdu.data.repository.SettingsRepositoryImpl
import com.sdu.izdesdu.domain.repository.SettingsRepository
import com.sdu.izdesdu.domain.usecase.GetUserSettingsUseCase
import com.sdu.izdesdu.domain.usecase.SetDarkThemeUseCase
import com.sdu.izdesdu.domain.usecase.SetPlaceSortOrderUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val settingsModule = module {
    single { SettingsDataStore(androidContext()) }
    single<SettingsRepository> { SettingsRepositoryImpl(get()) }
    single { GetUserSettingsUseCase(get()) }
    single { SetDarkThemeUseCase(get()) }
    single { SetPlaceSortOrderUseCase(get()) }
}

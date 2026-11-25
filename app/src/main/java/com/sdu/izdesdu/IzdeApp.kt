package com.sdu.izdesdu

import android.app.Application
import com.sdu.izdesdu.di.appModule
import com.sdu.izdesdu.di.networkModule
import com.sdu.izdesdu.di.settingsModule
import com.sdu.izdesdu.di.weatherModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class IzdeApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@IzdeApp)
            modules(appModule, networkModule, weatherModule, settingsModule)
        }
    }
}

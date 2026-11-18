package com.sdu.izdesdu

import android.app.Application
import com.sdu.izdesdu.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class IzdeApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@IzdeApp)
            modules(appModule)
        }
    }
}

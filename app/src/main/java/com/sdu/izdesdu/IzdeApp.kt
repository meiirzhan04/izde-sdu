package com.sdu.izdesdu

import android.app.Application
import com.sdu.izdesdu.di.AppContainer

class IzdeApp : Application() {
    val container by lazy { AppContainer(this) }
}
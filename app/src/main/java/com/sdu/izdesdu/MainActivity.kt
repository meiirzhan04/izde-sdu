package com.sdu.izdesdu

import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sdu.izdesdu.domain.usecase.GetUserSettingsUseCase
import org.koin.compose.koinInject
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.sdu.izdesdu.presentation.navigation.IzdeNavHost
import com.sdu.izdesdu.presentation.theme.IzdesduTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val getSettings = koinInject<GetUserSettingsUseCase>()
            val storedSettings by remember(getSettings) { getSettings() }
                .collectAsStateWithLifecycle(initialValue = null)
            val settings = storedSettings ?: return@setContent
            LaunchedEffect(settings.isDarkTheme) {
                val bars = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT) { settings.isDarkTheme }
                enableEdgeToEdge(statusBarStyle = bars, navigationBarStyle = bars)
            }
            IzdesduTheme(darkTheme = settings.isDarkTheme) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    IzdeNavHost(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
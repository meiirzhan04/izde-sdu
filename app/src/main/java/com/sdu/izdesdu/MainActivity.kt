package com.sdu.izdesdu

import android.os.Bundle
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
            IzdesduTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    IzdeNavHost(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
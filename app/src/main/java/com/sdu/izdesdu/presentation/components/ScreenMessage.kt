package com.sdu.izdesdu.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sdu.izdesdu.R

@Composable
fun ScreenMessage(
    @StringRes message: Int,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    messageColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = stringResource(message), color = messageColor)
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(onClick = onBack) {
                Text(text = stringResource(R.string.route_back))
            }
        }
    }
}

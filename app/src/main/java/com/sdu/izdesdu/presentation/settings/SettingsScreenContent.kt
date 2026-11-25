package com.sdu.izdesdu.presentation.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.sdu.izdesdu.R
import com.sdu.izdesdu.domain.model.PlaceSortOrder
import com.sdu.izdesdu.domain.model.UserSettings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenContent(
    settings: UserSettings,
    onBack: () -> Unit,
    onDarkThemeChanged: (Boolean) -> Unit,
    onSortOrderSelected: (PlaceSortOrder) -> Unit
) {
    Column {
        TopAppBar(
            title = { Text(stringResource(R.string.settings_title)) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.route_back))
                }
            },
            windowInsets = WindowInsets(0)
        )
        Column(Modifier.verticalScroll(rememberScrollState())) {
            SectionTitle(R.string.settings_appearance)
            ListItem(
                headlineContent = { Text(stringResource(R.string.settings_dark_theme)) },
                supportingContent = { Text(stringResource(R.string.settings_theme_description)) },
                trailingContent = { Switch(checked = settings.isDarkTheme, onCheckedChange = null) },
                modifier = Modifier.toggleable(settings.isDarkTheme, role = Role.Switch, onValueChange = onDarkThemeChanged)
            )
            SectionTitle(R.string.settings_sort)
            Column(Modifier.selectableGroup()) {
                PlaceSortOrder.entries.forEach { order ->
                    val description = when (order) {
                        PlaceSortOrder.DEFAULT -> R.string.sort_default_description
                        PlaceSortOrder.TITLE_ASC -> R.string.sort_title_description
                        PlaceSortOrder.QUESTIONS_ASC -> R.string.sort_short_description
                        PlaceSortOrder.QUESTIONS_DESC -> R.string.sort_long_description
                    }
                    val selected = settings.placeSortOrder == order
                    ListItem(
                        headlineContent = { Text(stringResource(order.titleRes)) },
                        supportingContent = { Text(stringResource(description)) },
                        trailingContent = { RadioButton(selected = selected, onClick = null) },
                        modifier = Modifier.selectable(selected, role = Role.RadioButton) { onSortOrderSelected(order) }
                    )
                }
            }
            Text(
                stringResource(R.string.settings_autosave),
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun SectionTitle(title: Int) {
    Text(
        stringResource(title),
        modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 4.dp),
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary
    )
}

package com.sdu.izdesdu.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.sdu.izdesdu.domain.model.PlaceSortOrder
import com.sdu.izdesdu.domain.model.UserSettings
import java.io.IOException
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

private val Context.settingsDataStore by preferencesDataStore(name = "izde_settings")
private val darkThemeKey = booleanPreferencesKey("is_dark_theme")
private val sortOrderKey = stringPreferencesKey("place_sort_order")

class SettingsDataStore internal constructor(private val store: DataStore<Preferences>) {
    constructor(context: Context) : this(context.applicationContext.settingsDataStore)

    val settings = store.data.catch { error ->
        if (error is IOException) emit(emptyPreferences()) else throw error
    }.map { preferences ->
        UserSettings(
            isDarkTheme = preferences[darkThemeKey] ?: false,
            placeSortOrder = PlaceSortOrder.entries.find { it.name == preferences[sortOrderKey] }
                ?: PlaceSortOrder.DEFAULT
        )
    }

    suspend fun setDarkTheme(enabled: Boolean) {
        store.edit { it[darkThemeKey] = enabled }
    }

    suspend fun setPlaceSortOrder(order: PlaceSortOrder) {
        store.edit { it[sortOrderKey] = order.name }
    }
}

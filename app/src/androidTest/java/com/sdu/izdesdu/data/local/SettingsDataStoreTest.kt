package com.sdu.izdesdu.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sdu.izdesdu.domain.model.PlaceSortOrder
import com.sdu.izdesdu.domain.model.UserSettings
import java.io.File
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SettingsDataStoreTest {
    @get:Rule
    val folder = TemporaryFolder()

    @Test
    fun reopeningPreferencesRestoresThemeAndOrder() = runBlocking {
        withStore { store ->
            val settings = SettingsDataStore(store)
            assertEquals(UserSettings(), settings.settings.first())
            settings.setDarkTheme(true)
            settings.setPlaceSortOrder(PlaceSortOrder.QUESTIONS_DESC)
        }
        withStore { store ->
            assertEquals(
                UserSettings(isDarkTheme = true, placeSortOrder = PlaceSortOrder.QUESTIONS_DESC),
                SettingsDataStore(store).settings.first()
            )
        }
    }

    private suspend fun withStore(block: suspend (DataStore<Preferences>) -> Unit) {
        val job = SupervisorJob()
        val store = PreferenceDataStoreFactory.create(scope = CoroutineScope(Dispatchers.IO + job)) {
            File(folder.root, "settings.preferences_pb")
        }
        try {
            block(store)
        } finally {
            job.cancelAndJoin()
        }
    }
}

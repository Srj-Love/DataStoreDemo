package com.sundaymobility.prefdatastore.utils

import android.content.Context
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.emptyPreferences
import androidx.datastore.preferences.preferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

enum class UiMode {
    LIGHT, DARK
}

class SettingManager(context: Context) {
    companion object {
        val IS_DARK_MODE = preferencesKey<Boolean>(Common.PREF_DARK_MODE)
    }

    private val dataStore = context.createDataStore(name = Common.PREF_NAME)

    val uiModeFlow: Flow<UiMode> = dataStore.data.catch {
        if (it is IOException) {
            it.printStackTrace()
            emit(emptyPreferences())
        } else {
            throw it
        }
    }.map { pref ->
        val isDarkMode = pref[IS_DARK_MODE] ?: false
        when (isDarkMode) {
            true -> UiMode.DARK
            false -> UiMode.LIGHT
        }
    }


    /**
     * Change your UI mode
     * @param:$uiMode is for Dark,Light
     */
    suspend fun changeUiMode(uiMode: UiMode) {
        dataStore.edit {
            it[IS_DARK_MODE] = when (uiMode) {
                UiMode.DARK -> true
                UiMode.LIGHT -> false
            }
        }
    }

}
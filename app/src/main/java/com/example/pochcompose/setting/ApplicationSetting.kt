package com.example.pochcompose.setting

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

private val USER_ONBOARDING_PREFERENCE = booleanPreferencesKey("onBoardPreferences")

interface ApplicationSetting {
    suspend fun getBoardingOnce(): Flow<Boolean>
    suspend fun saveBoarding(store: Boolean)

}

class ApplicationSettingImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
 ) : ApplicationSetting {

    override suspend fun getBoardingOnce(): Flow<Boolean> {
        return dataStore.data
            .catch {exception->
                if(exception is IOException){
                    emit(emptyPreferences())
                }
                else{
                    throw exception
                }
            }.map { preferences ->
           val onBoarding = preferences[USER_ONBOARDING_PREFERENCE] ?: false
                onBoarding
        }
    }

    override suspend fun saveBoarding(store: Boolean) {
        dataStore.edit { preference ->
            preference[USER_ONBOARDING_PREFERENCE] = store
        }
    }

}

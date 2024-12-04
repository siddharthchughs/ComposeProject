package com.example.pochcompose.setting

import androidx.lifecycle.ViewModel
import com.example.pochcompose.BuildConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

sealed interface SettingUIState {
    sealed interface SettingItems
    data object CrashItem : SettingItems
    data class LogoutItem(val isLogged: Boolean) : SettingItems
    data object NotRequired : SettingUIState
   data class Loaded(val list: List<SettingItems>) : SettingUIState
}


@HiltViewModel
class SettingsViewModel@Inject constructor(): ViewModel() {

    val settingUIState: Flow<SettingUIState> = flowOf(
        transform()
    )

    private fun transform(): SettingUIState {
        val settingItems: MutableList<SettingUIState.SettingItems> = mutableListOf(
        )
        if (BuildConfig.DEBUG) {
            settingItems.add(
                SettingUIState.CrashItem
            )

            settingItems.add(
                SettingUIState.LogoutItem(false)
            )
        }
        return SettingUIState.Loaded(list = settingItems)
    }

    fun crash() {
        throw Exception()
    }

}
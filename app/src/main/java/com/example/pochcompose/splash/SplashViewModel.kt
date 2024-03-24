package com.example.pochcompose.splash

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pochcompose.setting.ApplicationSetting
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface SplashUIState{

    data object Loading: SplashUIState
    data object Loaded : SplashUIState

}



@HiltViewModel
class SplashViewModel @Inject constructor(
    private val applicationSetting: ApplicationSetting
): ViewModel() {

    val onBoardingCompleted =   mutableStateOf(false)
    val splashUIState: Flow<SplashUIState> = flow {
       emit(transform())
    }

    private suspend fun transform():SplashUIState{
       viewModelScope.launch {
        onBoardingCompleted.value = applicationSetting.getBoardingOnce().first()
       }
        return SplashUIState.Loaded
    }
}
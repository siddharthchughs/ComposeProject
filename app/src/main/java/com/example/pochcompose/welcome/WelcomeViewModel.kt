package com.example.pochcompose.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pochcompose.R
import com.example.pochcompose.setting.ApplicationSetting
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface WelcomeUIState {
    data object Loading : WelcomeUIState
    data class Loaded(val onBoardingList: List<WelcomeOnBoard>) : WelcomeUIState
}
data class WelcomeOnBoard(
    val label: String,
    val image: Int
)

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val applicationSetting: ApplicationSetting
) : ViewModel() {
    val welcomeUIState: Flow<WelcomeUIState> = flowOf(
        transform(list = onBoardingLabels)
    )

    private fun transform(list: List<WelcomeOnBoard>): WelcomeUIState {
        return WelcomeUIState.Loaded(list)
    }

    fun onBoardingOnce(isBoolean: Boolean){
        viewModelScope.launch {
            applicationSetting.saveBoarding(isBoolean)
        }
    }
}

val onBoardingLabels = listOf(
    WelcomeOnBoard(
        label = "Greetings",
        image = R.drawable.greetings
    ),
    WelcomeOnBoard(
        label = "Explore",
        image = R.drawable.explore
    ),
    WelcomeOnBoard(
        label = "Power",
        image = R.drawable.power
    ),
)
package com.example.pochcompose.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Loaded(val heroesList: List<HeroesUIState>) : HomeUiState
}

data class HeroesUIState(
    val id: Int,
    val name: String,
    val image: String,
    val about: String,
    val rating: Double,
    val power: Int,
    val month: String,
    val day: String,
    val family: List<String>,
    val abilities: List<String>,
    val natureTypes: List<String>
)

@HiltViewModel
class HomeViewModel @Inject constructor():ViewModel(){
    val homeUiState: Flow<HomeUiState> = flowOf(
        tramsform(emptyList())
    )

    private fun tramsform(list: List<HeroesUIState>):HomeUiState{
        return HomeUiState.Loaded(list)
    }
}
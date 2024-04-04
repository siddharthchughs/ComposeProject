package com.example.pochcompose.home

import androidx.lifecycle.ViewModel
import com.example.pochcompose.setting.ApplicationSetting
import com.example.pochcompose.utility.imageLoad
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Loaded(val heroesList: List<HeroesUIState>) : HomeUiState

    data class TerminalError(
        val error: String
    ) : HomeUiState
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
class HomeViewModel @Inject constructor(
    private val heroesRepository: HeroesRepository,
    applicationSetting: ApplicationSetting
) : ViewModel() {


    val homeUiState: Flow<HomeUiState> = flow {
        emit(transform(applicationSetting.getBaseUrl().first()))
    }

    private suspend fun transform(baseUrl:String): HomeUiState {
        return when (val heroesResponse = heroesRepository.getAllHeroes()) {
            is HeroesNetworkResult.AvailableHeroesList -> {
                HomeUiState.Loaded(
                    heroesList = heroesResponse.heroes.heroes.map {
                        HeroesUIState(
                            id = it.id,
                            name = it.name,
                            image = baseUrl.imageLoad(it.image),
                            about = it.about,
                            rating = it.rating.toDouble(),
                            power = it.power,
                            month = it.month,
                            day = it.day,
                            family = listOf(it.family.toString()),
                            abilities = listOf(it.abilities.toString()),
                            natureTypes = listOf(it.natureTypes.toString())
                        )
                })
            }

            is HeroesNetworkResult.UnAuthorizedError -> {
                HomeUiState.TerminalError(error = heroesResponse.code.toString())
            }

            is HeroesNetworkResult.UnAuthorizedResponse -> {
                HomeUiState.TerminalError(error = heroesResponse.message)
            }
        }
    }
}
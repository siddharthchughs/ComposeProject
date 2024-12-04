package com.example.pochcompose.home

import androidx.lifecycle.ViewModel
import com.example.pochcompose.database.HeroesDatabase
import com.example.pochcompose.setting.ApplicationSetting
import com.example.pochcompose.utility.imageLoad
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Loaded(val heroesList: List<HeroesUIState>) : HomeUiState

    data class TerminalError(
        val error: String
    ) : HomeUiState

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

}



@HiltViewModel
class HomeViewModel @Inject constructor(
    private val heroesRepository: HeroesRepository,
    private val repository: Repository,
    private val heroesDatabase: HeroesDatabase,
    applicationSetting: ApplicationSetting
) : ViewModel() {
    val homeUiState: Flow<HomeUiState> = flow {
        emit(transform(applicationSetting.getBaseUrl().toString()))
    }


    private suspend fun transform(baseUrl: String): HomeUiState {
        return when (val response = heroesRepository.getAllHeroes()) {

            is HeroesNetworkResult.AvailableHeroesList -> {
                val heroList = response.heroesResponse.heroes.map {
                    Timber.i("Hero :: ${response.heroesResponse}")
//                    transform(baseUrl = baseUrl, hero = hero)
                    HomeUiState.HeroesUIState(
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
                }
//                 heroesRepository.insertIntoHeroes(response.heroesResponse.heroes)
                HomeUiState.Loaded(heroesList = heroList)
            }

            is HeroesNetworkResult.UnAuthorizedError -> {
                HomeUiState.TerminalError(response.code.toString())
            }

            is HeroesNetworkResult.UnAuthorizedResponse -> {
                HomeUiState.TerminalError(response.message)
            }
        }
    }

    private fun transform(baseUrl: String, hero: HomeUiState.HeroesUIState): HomeUiState.HeroesUIState {
        return HomeUiState.HeroesUIState(
            id = hero.id,
            name = hero.name,
            image = baseUrl.imageLoad(hero.image),
            about = hero.about,
            rating = hero.rating.toDouble(),
            power = hero.power,
            month = hero.month,
            day = hero.day,
            family = listOf(hero.family.toString()),
            abilities = listOf(hero.abilities.toString()),
            natureTypes = listOf(hero.natureTypes.toString())
        )
    }
}

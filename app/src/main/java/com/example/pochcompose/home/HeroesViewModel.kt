package com.example.pochcompose.home

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.example.pochcompose.setting.ApplicationSetting
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


sealed interface HeroUIState {
    data object Loading : HeroUIState
    data class Loaded(val heroes: PagingData<HeroState>) : HeroUIState

    data class TerminalError(
        val error: String
    ) : HeroUIState

    data class HeroState(
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
class HeroesViewModel @Inject constructor(
    private val repository: Repository,
    private val applicationSetting: ApplicationSetting,
    private val heroesRepository: HeroesRepository
) : ViewModel() {


//    val heroUiState : StateFlow<HeroUIState> = repository.getAllHeroes()
//        .map {it->
////          HeroUIState.Loaded(heroes = it)
//        }.stateIn(
//            scope = viewModelScope,
//            initialValue = HeroUIState.Loading,
//            started = SharingStarted.WhileSubscribed(5_000L)
//        )


//    private fun transform(baseUrl:String,hero: HeroState): HeroUIState =
//            HeroState(
//                id = hero.id,
//                name = hero.name,
//                image = baseUrl.imageLoad(hero.image),
//                rating = hero.rating,
//                about = hero.about,
//                power = hero.power,
//                month = hero.month,
//                day = hero.day,
//                family = listOf(hero.family.toString()),
//                abilities = listOf(hero.abilities.toString()),
//                natureTypes = listOf(hero.natureTypes.toString())
//            )
//
//
   val heroesFlow = repository.getAllHeroes()
}


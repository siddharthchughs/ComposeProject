package com.example.pochcompose.search

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pochcompose.setting.ApplicationSetting
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

sealed interface SearchUIState {
    data object Loading : SearchUIState
    data class Loaded(val listBySearch: List<SearchState>) : SearchUIState
    data class SearchTerminalError(
        val searchError: String
    ) : SearchUIState
}

data class SearchState(
    val searchHeroID: String,
    val searchHeroName: String
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchHeroRepository: SearchHeroRepository,
    private val applicationSetting: ApplicationSetting,

    ) : ViewModel() {

      val searchText = mutableStateOf("")

    fun onSearchTextChange(search: String) {
        searchText.value = search
    }

    @OptIn(FlowPreview::class)
    val searchUIState  = flow {
      emit(SearchUIState.Loading)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = viewModelScope
    )

        private suspend fun onSearch(search: String): SearchUIState {
        return when (val searchResponse = searchHeroRepository.searchByHero(search)) {
            is SearchNetworkResult.AvailableForSearch -> {
                val searchHero = searchResponse.searchHero.heroes.map {
                    SearchState(
                        searchHeroID = it.id.toString(),
                        searchHeroName = it.name,

                        )
                }
                SearchUIState.Loaded(searchHero)
            }

            is SearchNetworkResult.UnexpectedError -> {
                SearchUIState.SearchTerminalError(searchError = searchResponse.code.toString())
            }

            is SearchNetworkResult.UnexpectedResponse -> {
                SearchUIState.SearchTerminalError(searchError = searchResponse.message.toString())

            }
        }
    }

    fun clearSearch() {
        searchText.value = ""
    }

}

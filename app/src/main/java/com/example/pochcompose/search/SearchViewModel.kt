package com.example.pochcompose.search

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

sealed interface SearchUIState{
    data object Loading: SearchUIState
    data class Loaded(val listBySearch: List<SearchState>) : SearchUIState
    data class SearchTerminalError(
        val searchError:String)
        : SearchUIState
}

data class SearchState(
    val searchHeroID:String,
    val searchHeroName:String,
    val heroImage:String
)

@HiltViewModel
class SearchViewModel @Inject constructor() : ViewModel() {

    var searchText = mutableStateOf("")


   val searchUIState: Flow<SearchUIState> = flow {

   }

    fun onSearchTextChange(search:String){
        searchText.value = search
    }





}
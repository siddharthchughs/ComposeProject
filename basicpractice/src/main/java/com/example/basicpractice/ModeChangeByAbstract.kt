package com.example.basicpractice

fun main() {
    RepositoryByAbstract.fetchData()
    changeMode(mode = RepositoryByAbstract.getCurrentData())
    RepositoryByAbstract.manageData()
    changeMode(mode = RepositoryByAbstract.getCurrentData())
    RepositoryByAbstract.error()
    changeMode(mode = RepositoryByAbstract.getCurrentData())
}

private fun changeMode(mode: ModeChangeByAbstract) {
    when (mode) {
        is Error ->{
            println(mode.exception.message)
        }

        is ManageLoadedData->{
            println(mode.manageDataFetched?: "Ensure you pass data")
        }

        is Loading->{
            println("Loading..")
        }

        is NotLoading->{
            println("Not Loading")
        }
        else -> println("N/A")

    }
}


object RepositoryByAbstract {
    private var initialState : ModeChangeByAbstract= NotLoading
    private var startLoading: String? = null

    fun fetchData() {
        initialState = Loading
        startLoading = "Data"
    }

    fun manageData() {
        initialState = ManageLoadedData(manageDataFetched = "Manage Loaded data ")
        startLoading = "Manage"
    }

    fun restrictedData() {
        initialState = NotLoading
        startLoading = null
    }

    fun error() {
        initialState = Error(exception = Exception("exe"))
    }

    fun getCurrentData(): ModeChangeByAbstract = initialState

}

abstract class ModeChangeByAbstract
    data class Loaded(val dataFetched:String?):ModeChangeByAbstract()
    data class ManageLoadedData(val manageDataFetched:String?):ModeChangeByAbstract()
    data class  Error(val exception: Exception):ModeChangeByAbstract()
    data object Loading: ModeChangeByAbstract()
    data object NotLoading : ModeChangeByAbstract()

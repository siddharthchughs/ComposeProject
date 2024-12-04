package com.example.basicpractice


fun main() {
    RepositoryBySealed.loadedData()
    changemode(mode1 = RepositoryBySealed.getCurrentData())
    RepositoryBySealed.manageData()
    changemode(mode1 = RepositoryBySealed.getCurrentData())
    RepositoryBySealed.restrictedData()
    changemode(mode1 = RepositoryBySealed.getCurrentData())
    RepositoryBySealed.error()
    changemode(mode1 = RepositoryBySealed.getCurrentData())
}

private fun changemode(mode1: ModeChangeBySealed) {
    when (mode1) {
        is ModeChangeBySealed.Loaded -> println(mode1.dataFetched)
        is ModeChangeBySealed.ManageLoadedData -> println(mode1.manageDataFetched)
        ModeChangeBySealed.NotLoading -> println("nothing available to oad !")
        is ModeChangeBySealed.Error -> println(mode1.exception.message)
    }
}


object RepositoryBySealed {
    private var initialState : ModeChangeBySealed= ModeChangeBySealed.NotLoading
    private var startLoading: String? = null

    fun loadedData() {
        initialState = ModeChangeBySealed.Loaded(dataFetched = "Data loaded")
        startLoading = "Loaded"
    }

    fun manageData() {
        initialState = ModeChangeBySealed.ManageLoadedData(manageDataFetched = "Manage Loaded data ")
        startLoading = "Manage"
    }

    fun restrictedData() {
        initialState = ModeChangeBySealed.NotLoading
        startLoading = null
    }

    fun error() {
        initialState = ModeChangeBySealed.Error(exception = Exception("exe"))
    }

    fun getCurrentData(): ModeChangeBySealed = initialState

}

sealed class ModeChangeBySealed {
    data class Loaded(val dataFetched: String?) : ModeChangeBySealed()
    data class ManageLoadedData(val manageDataFetched: String?) : ModeChangeBySealed()
    data class Error(val exception: Exception) : ModeChangeBySealed()
    data object NotLoading : ModeChangeBySealed()
}
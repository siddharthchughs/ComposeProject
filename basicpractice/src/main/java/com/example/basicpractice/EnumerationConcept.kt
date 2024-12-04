package com.example.basicpractice

fun main() {
    Repository.error()
    changeMode(mode = Repository.getCurrentData())
}

private fun changeMode(mode: Mode) {
    when (mode) {
        Mode.RESTRICTED -> {
            println(Mode.RESTRICTED)
        }

        Mode.MANAGE -> {
            println(Mode.MANAGE)
        }

        Mode.IDLE -> {
            println(Mode.IDLE)

        }

        Mode.LOADING -> {
            println(Mode.LOADING)
        }

        Mode.ERROR -> {
            println(Mode.ERROR)
        }
    }
}


 object Repository {
    private var initialState:Mode = Mode.IDLE
    private var startLoading: String? = null
    fun fetchData() {
        initialState = Mode.LOADING
        startLoading = "Data"
    }

    fun manageData() {
        initialState = Mode.MANAGE
        startLoading = "Manage"
    }

    fun restrictedData() {
        initialState = Mode.RESTRICTED
        startLoading = null
    }

    fun error() {
        initialState = Mode.ERROR
    }

    fun getCurrentData(): Mode = initialState

}

enum class Mode {
    RESTRICTED,
    MANAGE,
    IDLE,
    LOADING,
    ERROR
}
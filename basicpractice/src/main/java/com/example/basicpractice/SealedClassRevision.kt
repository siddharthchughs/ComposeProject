package com.example.basicpractice

import java.io.IOException

fun main(args: Array<String>) {

    SelectionRepository.idleState()
    getResultMode(selectionOfMode = SelectionRepository.getCurrentState())
    SelectionRepository.getData()
    getResultMode(selectionOfMode = SelectionRepository.getCurrentState())
    SelectionRepository.finishedFetchingData()
    getResultMode(selectionOfMode = SelectionRepository.getCurrentState())
    SelectionRepository.error()
    getResultMode(selectionOfMode = SelectionRepository.getCurrentState())
    SelectionRepository.customFailure()
    getResultMode(selectionOfMode = SelectionRepository.getCurrentState())
}

fun getResultMode(selectionOfMode: ResultModeStates) {
    return when (selectionOfMode) {
        is ErrorState -> {
            println(selectionOfMode.exception)
        }

        is LoadingState -> {
            println("Loading...")
        }

        is LoadedState -> {
            println(selectionOfMode.message ?: "Ensure your are connected!!")
        }

        is IdleState -> {
            println("Idle")
        }

        is NotLoadingState -> {
            println("Nothing loaded")
        }

        is AnotherCustomException -> {
            println("NUll pointer Exception ${selectionOfMode.nullPointerException} ")
        }

        is CustomException -> {
            println("IOexception ${selectionOfMode.ioException}")
        }
    }
}


object SelectionRepository {
    private var loadState: ResultModeStates = NotLoadingState
    private var dataFetched: String? = null

    fun idleState() {
        loadState = IdleState
        dataFetched = "Idle"
    }

    fun getData() {
        loadState = LoadingState
        dataFetched = "Loaded"
    }

    fun finishedFetchingData() {
        loadState = LoadedState(dataFetched)
        dataFetched = null
    }

    fun error() {
        loadState = ErrorState(Exception("Exception"))
        dataFetched = "Error"
    }

    fun customFailure() {
        loadState = CustomException(ioException = IOException("IOException"))
        dataFetched = "Error"
    }

    fun getCurrentState(): ResultModeStates {
        return loadState
    }
}

sealed class ResultModeStates
data object IdleState : ResultModeStates()
data object LoadingState : ResultModeStates()
data object NotLoadingState : ResultModeStates()
data class LoadedState(val message: String?) : ResultModeStates()
data class ErrorState(val exception: Exception) : ResultModeStates()
sealed class CustomFailure : ResultModeStates()
data class CustomException(val ioException: IOException) : CustomFailure()
data class AnotherCustomException(val nullPointerException: NullPointerException) : CustomFailure()




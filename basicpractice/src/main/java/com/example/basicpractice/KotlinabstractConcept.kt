package com.example.basicpractice

fun main(args: Array<String>) {

    SelectionOfRepository.idleState()
    getResult(selectionOfMode = SelectionOfRepository.getCurrentState())
    SelectionOfRepository.getData()
    getResult(selectionOfMode = SelectionOfRepository.getCurrentState())
    SelectionOfRepository.finishedFetchingData()
    getResult(selectionOfMode = SelectionOfRepository.getCurrentState())
    SelectionOfRepository.error()
    getResult(selectionOfMode = SelectionOfRepository.getCurrentState())

}

fun getResult(selectionOfMode: SelectionOfMode){
    return when(selectionOfMode){

        is ErrorInLoading -> {
            println(selectionOfMode.exception)
        }
        is  LoadingFornow->{
            println("Loading...")

        }
        is  LoadedLastly->{
            println(selectionOfMode.message ?: "Ensure your are connected!!")

        }

        is Idle ->{
            println("Idle")

        }

        else -> {
            println("N/a")
        }
    }
}




object SelectionOfRepository{
    private var loadState: SelectionOfMode = NotLoadingTryAgin
    private var dataFetched :String? = null

    fun idleState(){
        loadState = Idle
        dataFetched = "Idle"
    }
    fun getData(){
        loadState = LoadingFornow
        dataFetched = "Loaded"
    }

    fun finishedFetchingData(){
        loadState = LoadedLastly(dataFetched)
        dataFetched = null
    }

    fun error(){
        loadState = ErrorInLoading(Exception("Exception"))
        dataFetched = "Error"
    }

    fun getCurrentState():SelectionOfMode{
        return loadState
    }
}

abstract class SelectionOfMode
     data object Idle : SelectionOfMode()
     data object LoadingFornow : SelectionOfMode()
     data object NotLoadingTryAgin : SelectionOfMode()
    data class LoadedLastly(val message: String?) : SelectionOfMode()
    data class ErrorInLoading(val exception: Exception) : SelectionOfMode()


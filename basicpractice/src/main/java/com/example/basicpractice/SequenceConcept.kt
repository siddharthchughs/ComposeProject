package com.example.basicpractice

fun main() {


    val immutableMap = mapOf(
        1 to Motor("Purple","Suzuki",2009),
        2 to Motor("Red","Toyota",2009),
        3 to Motor("Maroon","Toyota",2020),
    )

    println(immutableMap.asSequence().filter {
        it.value.model == "Toyota"
    }
     .map {
            it.value.name
        }
    )
    println(listOf("a","b","v").asSequence()
        .filter { println("Filtering $it"); it[0]== 'b' }
        .map { println("mapping $it"); it.toUpperCase()}
    )

}
data class Motor(
    val name:String,
    val model:String,
    val year:Int
) {
}
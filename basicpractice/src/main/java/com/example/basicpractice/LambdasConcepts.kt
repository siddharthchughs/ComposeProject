package com.example.basicpractice

fun main(args:Array<String>){
    println("Hello")
    println( countT1000Apply())
}

fun countT1000(): String{
    val numbers = StringBuilder()
    for(i in 1..1000){
       numbers.append(i)
       numbers.append(",")
    }
    numbers.append(100)
    return numbers.toString()
}

/*
*
* */
fun countT1000with(): String {
    val numbers = StringBuilder()
    return with(numbers) {
        for (i in 1..1000) {
            append(i)
            append(",")
        }
        append(100)
        toString()
    }
}

fun countT1000withfunctionAsExpression() =
    with(StringBuilder()) {
        for (i in 1..1000) {
            append(i)
            append(",")
        }
        append(100)
        toString()
    }

fun countT1000Apply() = StringBuilder().apply {
        for (i in 1..1000) {
            append(i)
            append(",")
        }
        append(100)
    }.toString()


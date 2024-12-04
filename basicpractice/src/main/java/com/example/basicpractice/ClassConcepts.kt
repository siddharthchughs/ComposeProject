package com.example.basicpractice

fun main(args: Array<String>) {

    val emp1 = Employees("Joan",true)
    println(emp1.firstname)
    println(emp1.fulltime)
    val emp2 = Employees("John",false)
    println(emp2.firstname)
    println(emp2.fulltime)

    var cars1 = CarInfo("Altis","grey",2051)
    println(cars1.name)
    println(cars1.color)
    var cars2 = CarInfo("Q7","grey",2051)
    var cars4 = CarInfo("Q8","grey",2051)
    println(cars2.name)
    println(cars2.color)
    println(cars1 == cars2)
    val cars3 = cars2.copy()
    println(cars3.hashCode()==cars2.hashCode())
    helloWorld()
   println(sumOFNumbers(3,4))
  printColor(cars1,cars2,cars3)

  val arrayOFCars  = arrayOf(cars1,cars2,cars3)
    printColor(*arrayOFCars)


}
fun printNames(vararg names: String) {
    for (name in names) {
        println(name)
    }
}




fun printColor(vararg cars: CarInfo){
    for (c in cars){
        println(c.name)
    }
}
fun helloWorld(){
    println("hey there !!")
}

fun sumOFNumbers(operand1:Int,operand2:Int):String=
     "${operand1 * operand2}"


data class CarInfo(
    val name:String,
    val color:String,
    val year:Int
)

class Employees(
    val firstname: String,
    val fulltime:Boolean = true
){
//    var fulltime: Boolean = true
//    constructor(firstname: String, fulltime:Boolean):this(firstname){
//        this.fulltime = fulltime
//    }
}
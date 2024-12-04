package com.example.basicpractice

fun main(args:Array<String>){

    val employee = Employee("Siddharth")
println(labelMultiplyfunctionAsBody(1,1,"Result"))
println(labelMultiplyfunctionAsExpressionBody(1,1,"Result"))
println(employee.nameInUppercase())

    val car1 = Cars("WV","Vento")
    val car2 = Cars("Skoda","Fabi")
    val car3 = Cars("Suzuiki","Starlet")

    val cars  = arrayOf(car1,car2,car3)

  // so we are passing the different cars instances to run multiple cars object to show name of different cars
  // however we could use spread operator to show the array of cars into the function,
  // as array takes the arguments in the form of vararg in kotlin
    printALlCars(*cars)
    printALlCars(car1,car2,car3)

}

fun printALlCars(vararg cars: Cars){
    for (i in cars){
        println(i.name)
    }
}

fun labelMultiplyfunctionAsBody(operand1:Int,operand2:Int,label:String):String{
    return ("$label $operand1 $operand2")
}

fun labelMultiplyfunctionAsExpressionBody(operand1:Int, operand2:Int, label:String):String=
    ("$label $operand1 $operand2")

class Employee(val firstname:String){
    fun nameInUppercase() = firstname.uppercase()
}



data class Cars(val name:String,val model:String){}
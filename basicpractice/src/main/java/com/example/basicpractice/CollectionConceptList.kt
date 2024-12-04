package com.example.basicpractice

import java.math.BigDecimal

fun main(args: Array<String>) {


    val arrayOfNumbers = Array(5) { i -> i * 10 }
   for (num in arrayOfNumbers) {
        println(num)
    }

    val arraywithoutForloop = Array(5) { i -> (i+1) * 10 }
          for (numbes in arraywithoutForloop){
              println(numbes)
          }

    val mixtureOFArry = arrayOf("Hello",22,BigDecimal(10.3),'c')
    for (m in mixtureOFArry){
        println(m)
    }



    val setInts = setOf(5, 6, 7, 78, 3404)
    println(setInts)

    /* how to add 10 within the list of items using map that returns the collection of the items
    *  from the set of elements after being filtered. */
    println(setInts.filter {
        it % 2 == 0
    }
        .map {
            it + 10
        }
    )

    /*
    * Alternative process to the map the elements for the adding the 10 to each iteration
    * */

    // 1
    val emptyarrayOfElement: MutableList<Int> = mutableListOf()
    for (eachIt in setInts) {
        if (eachIt % 2 == 0) {
            emptyarrayOfElement.add(eachIt + 10)
        }
    }
    println(emptyarrayOfElement)


    val lists = listOf("spring", "summer", "fall", "Winter")
    println(lists.javaClass)
    println(lists[2])
    println(lists.indexOf("autumn"))



//
//    var listsCouldBeNull = listOf("spring", null, "fall", "Winter")
//    println(listsCouldBeNull.javaClass)
//
//    val emptyList = emptyList<String>()
//    println(emptyList.javaClass)
//
//    val array = arrayOf("a","b","c")
//    array.set(2,"y")
//    println(array)
//    println("siz eof the array is :"+ array.size)
//    val colorList = array.toList()
//
//    //val colorList = listOf(*array)
//    println(colorList)
//
    val immutableMap = mapOf(
        1 to Car("Purple", "Suzuki", 2009),
        2 to Car("Red", "Toyota", 2009),
        3 to Car("Maroon", "Toyota", 2020),
    )

    println(immutableMap.filter { it.value.model == "Toyota" }
        .map { it.value.name }
    )

    val emptyList: MutableList<Int> = mutableListOf()
    println(immutableMap.map { it.value.year })
    for (i in immutableMap) {
        emptyList.add(i.value.year)
    }
    println(emptyList)

    println(immutableMap.javaClass)
    println(immutableMap.entries)
//
    val mutableMap = mutableMapOf<String, Car>(
        "Jessica" to Car("Purple", "Suzuki", 2009),
        "Benth" to Car("Red", "Toyota", 2009),
        "Cathrine" to Car("Maroon", "Toyota", 2020),
    )




    println("Before adding the size of the mutableMap was : ${mutableMap.size}")
    mutableMap.put("Will", Car(name = "Chevy", model = "GM", year = 2008))
//
//    for(i in mutableMap.keys){
//        println("Car of $i is ${mutableMap[i]?.name}, Model name is ${mutableMap[i]?.model} and Year of Manuf ${mutableMap[i]?.year}")
//    }
//
//    println("After adding the size of the mutableMap was : ${mutableMap.size}")
//
//    val mutableList = mutableListOf<String>(
//        "J0","S","J"
//    )
//    mutableList.add("g")
//    println(mutableList.size)
//    println(mutableList)
//
//
//    println(mutableMap.javaClass)
//    println(mutableMap)
//
//    val pair = Pair(1,10)
//    val (firstValue,secondValue) = pair
////    val firstValue = pair.first
////    val secondValue = pair.second
//    println("$firstValue  $secondValue")
//
//
// val hashMapOf = hashMapOf<String,Car>(
//     "Jessica" to Car("Purple","Suzuki",2009),
//     "Benth" to Car("Red","Toyota",2009),
//     "Cathrine" to Car("Maroon","Toyota",2020),
//     )
//
// val hashMapOfInts = hashMapOf<Int,Car>(
//     1 to Car("Purple","Suzuki",2009),
//     2 to Car("Red","Toyota",2009),
//     3 to Car("Maroon","Toyota",2020),
//     )
//
// val color = Car("Purple","Suzuki",2009)
//  val (name,model,year) = color
//
//
//    for((c,d) in hashMapOfInts){
//        println(" $c, value $d")
//    }
//
//    for(entry in hashMapOfInts){
//        println(" ${entry.key}, entry value ${entry.value}")
//    }
//    println(hashMapOf.javaClass)
//    println(hashMapOfInts)
//
//    val mutableListOfStrings = mutableListOf(1,2)
//    println(mutableListOfStrings.javaClass)
//    println("before addition "+mutableListOfStrings)
//    mutableListOfStrings.add(2,1)
//    println("after additon ::"+mutableListOfStrings)
//
//    println(mutableListOfStrings.javaClass)
//    println("mutable list of Strings :: $mutableListOfStrings")

    val array = arrayOf("apple", "banana", "orange")
    val pairs = ArrayList<Pair<String, Int>>()
    println("size of array : $array")
    for (element in array) {
        pairs.add(Pair(element, element.length))
    }

    var str :String? = null
    str = "Sid"
    println(str)
    println(pairs)

}

//class Car(
//    val name:String,
//    val model:String,
//    val year:Int
//){
//    operator fun component1()  = name
//    operator fun component2()  = model
//    operator fun component3()  = year
//}
data class Car(
    val name: String,
    val model: String,
    val year: Int
) {
}
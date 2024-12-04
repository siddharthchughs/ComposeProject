package com.example.basicpractice.Utility

fun uppercaseString(uppercaseString: String):String{
    val uppercaseFromLeft = uppercaseString.substring(0,1).uppercase()+uppercaseString.substring(1)
    return uppercaseFromLeft.substring(0,uppercaseFromLeft.length-1)+
            uppercaseFromLeft.substring(uppercaseFromLeft.length-1).toUpperCase()
}

fun String.uppercaseExString():String{
    val uppercaseFromLeft = this.substring(0,1).uppercase()+this.substring(1)
    return uppercaseFromLeft.substring(0,uppercaseFromLeft.length-1)+
            uppercaseFromLeft.substring(uppercaseFromLeft.length-1).toUpperCase()
}

fun String.uppercaseOddNUmberString():String{
    val uppercaseFromLeft = this.substring(0,1).uppercase()+this.substring(1)
    return uppercaseFromLeft.substring(0,uppercaseFromLeft.length-1)+
            uppercaseFromLeft.substring(uppercaseFromLeft.length-1).toUpperCase()
}
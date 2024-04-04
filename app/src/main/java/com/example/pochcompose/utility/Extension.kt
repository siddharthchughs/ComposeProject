package com.example.pochcompose.utility

import androidx.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.pochcompose.ui.theme.Purple40
import com.example.pochcompose.ui.theme.Purple80

val Colors.activeIndicators
    @Composable
    get() = if(isLight) Purple80 else Purple40

val Colors.inactiveIndicators
    @Composable
    get() = if(isLight) Purple40 else Color.DarkGray


fun String.imageLoad(heroImage:String):String{
    return this+heroImage
}
package com.example.tictactoe.ui.theme

import android.graphics.Color.parseColor
import androidx.compose.ui.graphics.Color

val Primary = "#f9d459".color
val onPrimary = "#ffffff".color
val Secondary = "#f22853".color
val onSecondary = "#ffffff".color
val Background = "#101516".color
val Tertiary = "#035cc2".color


val String.color
    get() = Color(parseColor(this))

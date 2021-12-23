package com.test.movies_test.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = blue700,
    onPrimary = white,
    background = black,
    surface = paleBlack,
    onSurface = white,
    onBackground = white

)

private val LightColorPalette = lightColors(
    primary = blue500,
    onPrimary = black,
    background = white,
    surface = paleWhite,
    onSurface = dark_gray,
    onBackground= black,


    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun ComposeMovieTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    MaterialTheme(
        colors = if(darkTheme)  DarkColorPalette else LightColorPalette

    ){
        content()

    }
//    val colors = if (darkTheme) {
//        DarkColorPalette
//    } else {
//        LightColorPalette
//    }
//
//    MaterialTheme(
//        colors = colors,
//        typography = Typography,
//        shapes = Shapes,
//        content = content
//    )
}
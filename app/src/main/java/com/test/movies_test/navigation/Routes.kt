package com.test.movies_test.navigation

sealed class Routes(val route: String) {
    object Home : Routes("home")
    object Details : Routes("details")
}
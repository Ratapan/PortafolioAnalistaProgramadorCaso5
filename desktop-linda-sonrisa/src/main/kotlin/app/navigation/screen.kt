package app.navigation

sealed class Screen {
    object Menu : Screen()
    object Login : Screen()
}